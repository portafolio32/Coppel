package com.coppel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CreateCommentRequestDTO {
	@JsonProperty("show_id")
	private Long showId;
    private String comment;
    private Double rating; // Validación sugerida: 0.0 a 5.0
}
