package com.finsight.banking.config;

import com.finsight.banking.model.LoanEntity;
import com.finsight.banking.model.TransactionEntity;
import com.finsight.banking.repository.LoanRepository;
import com.finsight.banking.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DemoDataInitializer implements CommandLineRunner {
    private final LoanRepository loans;
    private final TransactionRepository transactions;

    public DemoDataInitializer(LoanRepository loans, TransactionRepository transactions) {
        this.loans = loans;
        this.transactions = transactions;
    }

    @Override
    public void run(String... args) {
        if (loans.count() == 0) {
            loans.save(new LoanEntity("LN1001","C1001","Arun Kumar",new BigDecimal("185000"),42,"OVERDUE"));
            loans.save(new LoanEntity("LN1002","C1002","Priya Sharma",new BigDecimal("92000"),12,"OVERDUE"));
            loans.save(new LoanEntity("LN1003","C1003","Rahul Verma",new BigDecimal("0"),0,"CLOSED"));
            loans.save(new LoanEntity("LN1004","C1004","Sara Joseph",new BigDecimal("340000"),67,"OVERDUE"));
        }
        if (transactions.count() == 0) {
            transactions.save(new TransactionEntity("TXN9001","C1001",new BigDecimal("15000"),"LOAN_REPAYMENT","SUCCESS",null,LocalDateTime.now().minusHours(2)));
            transactions.save(new TransactionEntity("TXN9002","C1002",new BigDecimal("8500"),"LOAN_REPAYMENT","FAILED","Insufficient available balance",LocalDateTime.now().minusHours(1)));
            transactions.save(new TransactionEntity("TXN9003","C1004",new BigDecimal("12000"),"LOAN_REPAYMENT","FAILED","Core banking timeout",LocalDateTime.now().minusMinutes(30)));
            transactions.save(new TransactionEntity("TXN9004","C1003",new BigDecimal("5000"),"TRANSFER","SUCCESS",null,LocalDateTime.now().minusHours(3)));
        }
    }
}