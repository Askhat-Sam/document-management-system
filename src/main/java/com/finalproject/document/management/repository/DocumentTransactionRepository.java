package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.TransactionDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTransactionRepository extends JpaRepository<TransactionDocument, Long> {
}
