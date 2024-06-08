package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.TransactionEntity;

import java.util.List;

public interface TransactionService{
    List<TransactionEntity> findAll(String keyword);
    void save(TransactionEntity transaction);
    List<TransactionEntity> findAllByUser(String userId);
}