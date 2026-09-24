package com.finsight.ai.model;
import java.util.List;
public record AskResponse(String answer,String intent,String toolUsed,List<?> data) {}