package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionUser;
import com.finalproject.document.management.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserTransactionServiceImpl implements UserTransactionService{
    @Autowired
    private UserTransactionRepository userTransactionRepository;
    @Override
    public List<TransactionDocument> findAll(String keyword) {
        return null;
    }


    public void save(TransactionDocument transaction) {

    }

    public void save(TransactionUser transaction) {
        userTransactionRepository.save(transaction);
    }
}
