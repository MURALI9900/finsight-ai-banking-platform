package com.finsight.ai.model;
import jakarta.validation.constraints.NotBlank;
public record AskRequest(@NotBlank String question) {}