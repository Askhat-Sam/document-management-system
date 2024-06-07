package com.finalproject.document.management.service;
import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.entity.TransactionUser;

import java.util.List;

public interface TransactionService <T extends TransactionEntity>  {
    List<T> findAll(String keyword);
   void save(T transaction);
    List<T> findAllTransactionsById(Long id);
    List<T> findAllByUser(String userId);

}
