package com.finsight.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRecord(
        String transactionId,
        String customerId,
        BigDecimal amount,
        String type,
        String status,
        String failureReason,
        LocalDateTime createdAt
) {}
