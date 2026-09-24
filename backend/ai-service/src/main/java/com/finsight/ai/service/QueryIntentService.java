package com.finsight.ai.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Map;
@Service
public class QueryIntentService {
 private final BankingToolClient tools;
 private final AiProvider aiProvider;
 public QueryIntentService(BankingToolClient tools,AiProvider aiProvider){this.tools=tools;this.aiProvider=aiProvider;}
 public QueryResult answer(String question){
  String q=question.toLowerCase(Locale.ROOT);
  if(q.contains("overdue")||q.contains("over due")){
   int days=extractDays(q,30); var data=tools.overdueLoans(days);
   return new QueryResult(aiProvider.generateAnswer(question,"Found "+data.size()+" loan(s) overdue by more than "+days+" days."),"OVERDUE_LOANS","getOverdueLoans",data);
  }
  if(q.contains("failed")&&(q.contains("transaction")||q.contains("payment"))){
   var data=tools.failedTransactions();
   return new QueryResult(aiProvider.generateAnswer(question,"Found "+data.size()+" failed transaction(s)."),"FAILED_TRANSACTIONS","getFailedTransactions",data);
  }
  if(q.contains("summary")||q.contains("total")||q.contains("overview")){
   var data=tools.summary();
   return new QueryResult(aiProvider.generateAnswer(question,"Here is the current banking operations summary."),"BANKING_SUMMARY","getBankingSummary",List.of(data));
  }
  return new QueryResult("I could not map the question to an approved banking operation. Try asking about overdue loans, failed transactions, or a banking summary.","UNKNOWN","NONE",List.of());
 }
 private int extractDays(String q,int fallback){
  String[] words=q.replaceAll("[^0-9 ]"," ").trim().split("\s+");
  for(String word:words) try{int value=Integer.parseInt(word);if(value>0&&value<=3650)return value;}catch(NumberFormatException ignored){}
  return fallback;
 }
 public record QueryResult(String answer,String intent,String toolUsed,List<?> data){}
}