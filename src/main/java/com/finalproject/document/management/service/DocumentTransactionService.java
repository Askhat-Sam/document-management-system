package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionEntity;

import java.util.List;

public interface DocumentTransactionService extends TransactionService{
    List<TransactionEntity> findAllByDocument(Long documentId);
    void save(TransactionEntity transactionEntity);
}