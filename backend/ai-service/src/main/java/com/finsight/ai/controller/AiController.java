package com.finsight.ai.controller;

import com.finsight.ai.model.AskRequest;
import com.finsight.ai.model.AskResponse;
import com.finsight.ai.service.QueryIntentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private final QueryIntentService queryService;

    public AiController(QueryIntentService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@Valid @RequestBody AskRequest request) {
        var result = queryService.answer(request.question());
        return new AskResponse(result.answer(), result.intent(), result.toolUsed(), result.data(), result.sources());
    }
}