package com.finsight.ai.service;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
class QueryIntentServiceTest {
 @Test void routesOverdueQuestionToApprovedTool(){
  BankingToolClient tools=mock(BankingToolClient.class); AiProvider ai=mock(AiProvider.class);
  when(tools.overdueLoans(30)).thenReturn(List.of(Map.of("loanId","LN1001")));
  when(ai.generateAnswer(anyString(),anyString())).thenReturn("Found 1 loan(s) overdue by more than 30 days.");
  var result=new QueryIntentService(tools,ai).answer("Show me loans overdue by more than 30 days");
  assertEquals("OVERDUE_LOANS",result.intent()); assertEquals("getOverdueLoans",result.toolUsed());
  assertEquals(1,result.data().size()); verify(tools).overdueLoans(30);
 }
}