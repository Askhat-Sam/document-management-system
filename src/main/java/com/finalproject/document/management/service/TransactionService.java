package com.finalproject.document.management.service;
import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionUser;

import java.util.List;

public interface TransactionService {
    <T>  List<T> findAll(String keyword);
   void save(TransactionUser transaction);

}
