package com.finsight.banking.repository;

import com.finsight.banking.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByStatusIgnoreCase(String status);
}