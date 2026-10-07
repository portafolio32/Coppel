package com.coppel.api.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "show_comments")
public class CommentDocument {

	    @Id
	    private String id;
	    
	    private Long showId;
	    private String comment;
	    private Double rating; // Valor entre 0 y 5
	    private LocalDateTime createdAt;
	}


