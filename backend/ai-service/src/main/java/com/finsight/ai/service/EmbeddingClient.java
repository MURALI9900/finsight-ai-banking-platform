package com.finsight.ai.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class EmbeddingClient {
    private final LlmProperties properties;
    private final RestClient client;

    public EmbeddingClient(LlmProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder().baseUrl(properties.baseUrl()).build();
    }

    public List<Double> embed(String text) {
        if (!properties.configured()) return List.of();

        Map<String,Object> request = Map.of(
                "model", properties.embeddingModel(),
                "input", text
        );

        Map<String,Object> response = client.post()
                .uri("/embeddings")
                .header("Authorization", "Bearer " + properties.apiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);

        try {
            List<Map<String,Object>> data = (List<Map<String,Object>>) response.get("data");
            List<Number> values = (List<Number>) data.get(0).get("embedding");
            return values.stream().map(Number::doubleValue).toList();
        } catch (Exception ex) {
            return List.of();
        }
    }
}