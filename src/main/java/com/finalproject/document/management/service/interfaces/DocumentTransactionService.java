package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.entity.TransactionEntity;

import java.util.List;

public interface DocumentTransactionService extends TransactionService {
    List<TransactionEntity> findAllByDocumentCode(String documentCode);
    List<TransactionEntity> findAllByUser(String userId);
    void save(TransactionEntity transactionEntity);
}