package com.finsight.ai.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "finsight.llm")
public record LlmProperties(String baseUrl, String apiKey, String model, String embeddingModel) {
    public boolean configured() {
        return apiKey != null && !apiKey.isBlank();
    }
}