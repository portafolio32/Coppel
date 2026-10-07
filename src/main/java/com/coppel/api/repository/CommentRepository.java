package com.coppel.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.coppel.api.model.CommentDocument;

@Repository
public interface CommentRepository extends MongoRepository<CommentDocument,String> {
	List<CommentDocument> findByShowId(Long showId);

    // Consulta en lote para optimizar las búsquedas del Endpoint A
    List<CommentDocument> findByShowIdIn(List<Long> showIds);

}
