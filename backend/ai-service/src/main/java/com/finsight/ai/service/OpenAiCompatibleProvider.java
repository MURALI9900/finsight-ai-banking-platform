package com.finsight.ai.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiCompatibleProvider implements AiProvider {
    private final LlmProperties properties;
    private final RestClient client;

    public OpenAiCompatibleProvider(LlmProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder().baseUrl(properties.baseUrl()).build();
    }

    @Override
    public String generateAnswer(String question, String toolResult) {
        if (!properties.configured()) {
            return toolResult + " (LLM not configured; using safe demo response.)";
        }

        String system = "You are FinSight AI, a banking operations assistant for authorized bank officers. "
                + "Answer only from the supplied banking tool result. Do not invent facts. "
                + "Be concise and explain that the result came from an approved banking tool.";

        Map<String, Object> request = Map.of(
                "model", properties.model(),
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content",
                                "Officer question: " + question + "\nApproved tool result: " + toolResult)
                ),
                "temperature", 0.1
        );

        Map<String, Object> response = client.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + properties.apiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = choices.get(0);
            Map<String, Object> content = (Map<String, Object>) message.get("message");
            return String.valueOf(content.get("content"));
        } catch (Exception ex) {
            return "The LLM response could not be parsed. Approved banking result: " + toolResult;
        }
    }
}