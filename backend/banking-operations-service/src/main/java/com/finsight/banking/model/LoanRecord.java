package com.finsight.banking.model;

import java.math.BigDecimal;

public record LoanRecord(
        String loanId,
        String customerId,
        String customerName,
        BigDecimal outstandingAmount,
        int daysOverdue,
        String status
) {}
