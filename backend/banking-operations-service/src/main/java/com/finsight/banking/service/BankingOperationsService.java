package com.finsight.banking.service;

import com.finsight.banking.model.LoanRecord;
import com.finsight.banking.model.TransactionRecord;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankingOperationsService {

    private final List<LoanRecord> loans = List.of(
            new LoanRecord("LN1001", "C1001", "Arun Kumar", new BigDecimal("185000.00"), 42, "OVERDUE"),
            new LoanRecord("LN1002", "C1002", "Priya Sharma", new BigDecimal("92000.00"), 12, "OVERDUE"),
            new LoanRecord("LN1003", "C1003", "Rahul Verma", new BigDecimal("0.00"), 0, "CLOSED"),
            new LoanRecord("LN1004", "C1004", "Sara Joseph", new BigDecimal("340000.00"), 67, "OVERDUE")
    );

    private final List<TransactionRecord> transactions = List.of(
            new TransactionRecord("TXN9001", "C1001", new BigDecimal("15000.00"), "LOAN_REPAYMENT", "SUCCESS", null, LocalDateTime.now().minusHours(2)),
            new TransactionRecord("TXN9002", "C1002", new BigDecimal("8500.00"), "LOAN_REPAYMENT", "FAILED", "Insufficient available balance", LocalDateTime.now().minusHours(3)),
            new TransactionRecord("TXN9003", "C1004", new BigDecimal("12000.00"), "LOAN_REPAYMENT", "FAILED", "Core banking timeout", LocalDateTime.now().minusHours(5)),
            new TransactionRecord("TXN9004", "C1003", new BigDecimal("5000.00"), "TRANSFER", "SUCCESS", null, LocalDateTime.now().minusHours(1))
    );

    public List<LoanRecord> getLoans() {
        return loans;
    }

    public List<LoanRecord> getOverdueLoans(int minimumDays) {
        return loans.stream()
                .filter(loan -> loan.daysOverdue() > minimumDays)
                .toList();
    }

    public List<TransactionRecord> getTransactions() {
        return transactions;
    }

    public List<TransactionRecord> getFailedTransactions() {
        return transactions.stream()
                .filter(tx -> "FAILED".equalsIgnoreCase(tx.status()))
                .toList();
    }

    public BigDecimal getTotalOutstanding() {
        return loans.stream()
                .map(LoanRecord::outstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
