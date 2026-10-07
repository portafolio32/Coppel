    package com.coppel.api.dto;
	import lombok.AllArgsConstructor;
	import lombok.Builder;
	import lombok.Data;
	import lombok.NoArgsConstructor;

	import java.util.List;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public class showSearchResponseDTO {
	    private Long id;
	    private String name;
	    private String channel;
	    private String summary;
	    private List<String> genres;
	    private List<CommentDTO> comments; 
	}


	