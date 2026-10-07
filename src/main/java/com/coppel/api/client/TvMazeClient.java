package com.coppel.api.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.coppel.api.dto.showSearchResponseDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
public class TvMazeClient {
	private final RestTemplate restTemplate;

    public TvMazeClient() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public List<showSearchResponseDTO> searchShows(String query) {
        String url = "http://api.tvmaze.com/search/shows?q=" + query;
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        if (response == null) return Collections.emptyList();

        List<showSearchResponseDTO> result = new ArrayList<>();

        for (Map<String, Object> item : response) {
            Map<String, Object> show = (Map<String, Object>) item.get("show");
            if (show == null) continue;

            Long id = ((Number) show.get("id")).longValue();
            String name = (String) show.get("name");
            String summary = (String) show.get("summary");
            List<String> genres = (List<String>) show.get("genres");

            String channel = null;
            if (show.get("network") != null) {
                Map<String, Object> network = (Map<String, Object>) show.get("network");
                channel = (String) network.get("name");
            } else if (show.get("webChannel") != null) {
                Map<String, Object> webChannel = (Map<String, Object>) show.get("webChannel");
                channel = (String) webChannel.get("name");
            }

            result.add(showSearchResponseDTO.builder()
                    .id(id)
                    .name(name)
                    .channel(channel)
                    .summary(summary)
                    .genres(genres)
                    .build());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getShowById(Long showId) {
        String url = "https://api.tvmaze.com/shows/" + showId;
        return restTemplate.getForObject(url, Map.class);
    }

}
