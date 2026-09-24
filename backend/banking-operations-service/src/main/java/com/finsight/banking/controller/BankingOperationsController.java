package com.finsight.banking.controller;

import com.finsight.banking.model.LoanRecord;
import com.finsight.banking.model.TransactionRecord;
import com.finsight.banking.service.BankingOperationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/banking")
public class BankingOperationsController {

    private final BankingOperationsService service;

    public BankingOperationsController(BankingOperationsService service) {
        this.service = service;
    }

    @GetMapping("/loans")
    public List<LoanRecord> loans() {
        return service.getLoans();
    }

    @GetMapping("/loans/overdue")
    public List<LoanRecord> overdueLoans(
            @RequestParam(defaultValue = "30") int minimumDays) {
        return service.getOverdueLoans(minimumDays);
    }

    @GetMapping("/transactions")
    public List<TransactionRecord> transactions() {
        return service.getTransactions();
    }

    @GetMapping("/transactions/failed")
    public List<TransactionRecord> failedTransactions() {
        return service.getFailedTransactions();
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        BigDecimal outstanding = service.getTotalOutstanding();
        return Map.of(
                "totalLoans", service.getLoans().size(),
                "totalTransactions", service.getTransactions().size(),
                "failedTransactions", service.getFailedTransactions().size(),
                "totalOutstandingAmount", outstanding
        );
    }
}
