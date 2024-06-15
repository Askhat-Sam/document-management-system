package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentTransactionRepository extends JpaRepository<TransactionDocument, Long> {
    void save(TransactionEntity transaction);
    List<TransactionEntity> findAllByDocumentCode(String documentCode);
    List<TransactionEntity> findAllByUser(String userId);
}
