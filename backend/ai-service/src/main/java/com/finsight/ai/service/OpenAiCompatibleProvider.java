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
        return generateAnswer(question, toolResult, List.of());
    }

    public String generateAnswer(String question, String toolResult, List<RagDocument> context) {
        if (!properties.configured()) {
            return toolResult + " (LLM not configured; using safe demo response.)";
        }

        String ragContext = context.isEmpty() ? "No policy context matched." :
                context.stream()
                    .map(d -> "[" + d.id() + "] " + d.title() + ": " + d.content())
                    .reduce("", (a, b) -> a + "\n" + b);

        String system = "You are FinSight AI, a banking operations assistant for authorized bank officers. "
                + "Answer only from the supplied banking tool result and policy context. "
                + "Do not invent facts. If policy context is insufficient, say so. "
                + "Keep the response concise and mention relevant policy IDs when useful.";

        Map<String, Object> request = Map.of(
                "model", properties.model(),
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content",
                                "Officer question: " + question
                                + "\nApproved banking tool result: " + toolResult
                                + "\nRetrieved policy context:" + ragContext)
                ),
                "temperature", 0.1
        );

        try {
            Map<String, Object> response = client.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + properties.apiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = choices.get(0);
            return String.valueOf(message.get("message") instanceof Map<?, ?> m ? m.get("content") : "No content");
        } catch (Exception ex) {
            return "The LLM is unavailable. Approved banking result: " + toolResult;
        }
    }
}