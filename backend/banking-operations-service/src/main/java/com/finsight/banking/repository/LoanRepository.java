package com.finsight.banking.repository;

import com.finsight.banking.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByDaysOverdueGreaterThan(int minimumDays);
}