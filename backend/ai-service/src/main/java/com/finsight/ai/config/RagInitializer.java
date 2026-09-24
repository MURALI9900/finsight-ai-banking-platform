package com.finsight.ai.config;

import com.finsight.ai.service.RagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RagInitializer implements CommandLineRunner {
    private final RagService ragService;

    public RagInitializer(RagService ragService) {
        this.ragService = ragService;
    }

    @Override
    public void run(String... args) {
        ragService.indexPolicies();
    }
}