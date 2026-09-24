package com.finsight.banking.service;

import com.finsight.banking.model.LoanEntity;
import com.finsight.banking.model.LoanRecord;
import com.finsight.banking.model.TransactionEntity;
import com.finsight.banking.model.TransactionRecord;
import com.finsight.banking.repository.LoanRepository;
import com.finsight.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankingOperationsService {
    private final LoanRepository loans;
    private final TransactionRepository transactions;

    public BankingOperationsService(LoanRepository loans, TransactionRepository transactions) {
        this.loans = loans;
        this.transactions = transactions;
    }

    public List<LoanRecord> getLoans() {
        return loans.findAll().stream().map(this::toRecord).toList();
    }

    public LoanRecord getLoan(String loanId) {
        return loans.findById(loanId).map(this::toRecord)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
    }

    public List<LoanRecord> getOverdueLoans(int minimumDays) {
        return loans.findByDaysOverdueGreaterThan(minimumDays).stream().map(this::toRecord).toList();
    }

    public List<TransactionRecord> getTransactions() {
        return transactions.findAll().stream().map(this::toRecord).toList();
    }

    public TransactionRecord getTransaction(String transactionId) {
        return transactions.findById(transactionId).map(this::toRecord)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
    }

    public List<TransactionRecord> getFailedTransactions() {
        return transactions.findByStatusIgnoreCase("FAILED").stream().map(this::toRecord).toList();
    }

    public BigDecimal getTotalOutstanding() {
        return loans.findAll().stream()
                .map(LoanEntity::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LoanRecord toRecord(LoanEntity e) {
        return new LoanRecord(e.getLoanId(), e.getCustomerId(), e.getCustomerName(),
                e.getOutstandingAmount(), e.getDaysOverdue(), e.getStatus());
    }

    private TransactionRecord toRecord(TransactionEntity e) {
        return new TransactionRecord(e.getTransactionId(), e.getCustomerId(), e.getAmount(),
                e.getType(), e.getStatus(), e.getFailureReason(), e.getCreatedAt());
    }
}