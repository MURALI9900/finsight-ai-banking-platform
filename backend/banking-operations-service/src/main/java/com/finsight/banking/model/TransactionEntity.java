package com.finsight.banking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    private String transactionId;
    private String customerId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String failureReason;
    private LocalDateTime createdAt;

    protected TransactionEntity() {}

    public TransactionEntity(String transactionId, String customerId, BigDecimal amount,
                             String type, String status, String failureReason, LocalDateTime createdAt) {
        this.transactionId=transactionId;
        this.customerId=customerId;
        this.amount=amount;
        this.type=type;
        this.status=status;
        this.failureReason=failureReason;
        this.createdAt=createdAt;
    }

    public String getTransactionId(){ return transactionId; }
    public String getCustomerId(){ return customerId; }
    public BigDecimal getAmount(){ return amount; }
    public String getType(){ return type; }
    public String getStatus(){ return status; }
    public String getFailureReason(){ return failureReason; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}