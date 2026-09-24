package com.finsight.banking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loans")
public class LoanEntity {
    @Id
    private String loanId;
    private String customerId;
    private String customerName;
    private BigDecimal outstandingAmount;
    private int daysOverdue;
    private String status;

    protected LoanEntity() {}

    public LoanEntity(String loanId, String customerId, String customerName,
                      BigDecimal outstandingAmount, int daysOverdue, String status) {
        this.loanId = loanId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.outstandingAmount = outstandingAmount;
        this.daysOverdue = daysOverdue;
        this.status = status;
    }

    public String getLoanId(){ return loanId; }
    public String getCustomerId(){ return customerId; }
    public String getCustomerName(){ return customerName; }
    public BigDecimal getOutstandingAmount(){ return outstandingAmount; }
    public int getDaysOverdue(){ return daysOverdue; }
    public String getStatus(){ return status; }
}