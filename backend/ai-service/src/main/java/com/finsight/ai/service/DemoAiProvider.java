package com.finsight.ai.service;
import org.springframework.stereotype.Component;
@Component
public class DemoAiProvider implements AiProvider {
 public String generateAnswer(String question,String toolResult){ return toolResult; }
}