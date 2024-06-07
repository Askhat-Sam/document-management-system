package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.entity.TransactionUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionRepository extends JpaRepository<TransactionUser, Long> {
    void save(TransactionEntity transaction);
}
