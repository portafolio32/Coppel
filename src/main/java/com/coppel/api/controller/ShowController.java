package com.coppel.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coppel.api.dto.CreateCommentRequestDTO;
import com.coppel.api.dto.showSearchResponseDTO;
import com.coppel.api.service.ShowService;
@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;

	public ShowController(ShowService showService) {
        this.showService = showService;
    }

    // Punto A - GET /api/shows/search?search_query=girls
    @GetMapping("/search")
    public ResponseEntity<List<showSearchResponseDTO>> searchShows(@RequestParam("search_query") String searchQuery) {
        List<showSearchResponseDTO> shows = showService.searchShows(searchQuery);
        return ResponseEntity.ok(shows);
    }

    // Punto B - GET /api/shows/{show_id}
    @GetMapping("/{show_id}")
    public ResponseEntity<Map<String, Object>> getShowById(@PathVariable("show_id") Long showId) {
        Map<String, Object> show = showService.getShowById(showId);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(show);
    }

    // Punto C - POST /api/shows/comments
    @PostMapping("/comments")
    public ResponseEntity<Map<String, String>> addComment(@RequestBody CreateCommentRequestDTO request) {
        try {
            showService.addComment(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("status", "SUCCESS"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("status", "ERROR: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("status", "ERROR"));
        }
    }
	
}
