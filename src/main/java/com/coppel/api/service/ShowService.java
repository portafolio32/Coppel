package com.coppel.api.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coppel.api.client.TvMazeClient;
import com.coppel.api.dto.CommentDTO;
import com.coppel.api.dto.CreateCommentRequestDTO;
import com.coppel.api.dto.showSearchResponseDTO;
import com.coppel.api.model.CommentDocument;
import com.coppel.api.model.ShowDocument;
import com.coppel.api.repository.CommentRepository;
import com.coppel.api.repository.ShowRepository;
@Service
public class ShowService {
	

	private final TvMazeClient tvMazeClient;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

    public ShowService(TvMazeClient tvMazeClient, 
                       ShowRepository showRepository, 
                       CommentRepository commentRepository) {
        this.tvMazeClient = tvMazeClient;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
    }

  
    public List<showSearchResponseDTO> searchShows(String query) {
        List<showSearchResponseDTO> shows = tvMazeClient.searchShows(query);

        if (shows.isEmpty()) {
            return shows;
        }

        List<Long> showIds = shows.stream()
                .map(showSearchResponseDTO::getId)
                .collect(Collectors.toList());

        List<CommentDocument> allComments = commentRepository.findByShowIdIn(showIds);

  
        Map<Long, List<CommentDTO>> commentsByShowId = allComments.stream()
                .collect(Collectors.groupingBy(
                        CommentDocument::getShowId,
                        Collectors.mapping(
                                c -> new CommentDTO(c.getComment(), c.getRating()),
                                Collectors.toList()
                        )
                ));

    
        shows.forEach(show -> {
            List<CommentDTO> comments = commentsByShowId.getOrDefault(show.getId(), Collections.emptyList());
            show.setComments(comments);
        });

        return shows;
    }


    // PUNTO B: Consulta Individual + Cache + Comentarios
  
    public Map<String, Object> getShowById(Long showId) {
        Map<String, Object> showDetails;

        //  Validar Cache Mongo
        Optional<ShowDocument> cachedShow = showRepository.findById(showId);
        if (cachedShow.isPresent()) {
            showDetails = new HashMap<>(cachedShow.get().getDetails());
        } else {
            //  Consumir API externa si no existe en Mongo
            showDetails = tvMazeClient.getShowById(showId);

            if (showDetails != null && !showDetails.isEmpty()) {
                ShowDocument newDocument = new ShowDocument();
                newDocument.setId(showId);
                newDocument.setDetails(showDetails);
                showRepository.save(newDocument);
                
                // Hacer una copia modificable del Map retornado
                showDetails = new HashMap<>(showDetails);
            }
        }

        if (showDetails == null || showDetails.isEmpty()) {
            return null;
        }

        //  Obtener comentarios desde Mongo y adjuntarlos al objeto
        List<CommentDTO> comments = commentRepository.findByShowId(showId)
                .stream()
                .map(c -> new CommentDTO(c.getComment(), c.getRating()))
                .collect(Collectors.toList());

        showDetails.put("comments", comments);

        return showDetails;
    }


    // PUNTO  Guardar Comentario y Rating
    public void addComment(CreateCommentRequestDTO request) {
        if (request.getRating() < 0 || request.getRating() > 5) {
            throw new IllegalArgumentException("El rating debe estar entre 0 y 5.");
        }

        CommentDocument commentDoc = CommentDocument.builder()
                .showId(request.getShowId())
                .comment(request.getComment())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(commentDoc);
    }
}
