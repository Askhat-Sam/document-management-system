package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.repository.UserTransactionRepository;
import com.finalproject.document.management.service.interfaces.UserTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserTransactionServiceImpl implements UserTransactionService {
    @Autowired
    private UserTransactionRepository userTransactionRepository;
    @Override
    public List<TransactionEntity> findAll(String keyword) {
        return null;
    }

    @Override
    public void save(TransactionEntity transaction) {
        userTransactionRepository.save(transaction);
    }


    public List<TransactionEntity>  findAllTransactionsById(Long id) {
        return userTransactionRepository.findAllById(id);
    }

    public List<TransactionEntity>  findAllByUser(String userId) {
        return userTransactionRepository.findAllByUser(userId);
    }

}
