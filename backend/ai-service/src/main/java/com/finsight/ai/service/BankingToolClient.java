package com.finsight.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Component
public class BankingToolClient {
 private final RestClient client;
 public BankingToolClient(@Value("${finsight.banking-service-url:http://localhost:8081}") String baseUrl){
  this.client=RestClient.builder().baseUrl(baseUrl).build();
 }
 public List<Map<String,Object>> overdueLoans(int minimumDays){
  return client.get().uri("/api/v1/banking/loans/overdue?minimumDays={days}",minimumDays).retrieve()
   .body(new ParameterizedTypeReference<>() {});
 }
 public List<Map<String,Object>> failedTransactions(){
  return client.get().uri("/api/v1/banking/transactions/failed").retrieve()
   .body(new ParameterizedTypeReference<>() {});
 }
 public Map<String,Object> summary(){
  return client.get().uri("/api/v1/banking/summary").retrieve()
   .body(new ParameterizedTypeReference<>() {});
 }
}