//package com.finalproject.document.management.service;
//
//import com.finalproject.document.management.entity.Transaction;
//import com.finalproject.document.management.entity.TransactionDocument;
//import com.finalproject.document.management.entity.TransactionUser;
//import com.finalproject.document.management.repository.DocumentTransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//@Service
//public class DocumentTransactionServiceImpl implements DocumentTransactionService{
//    @Autowired
//    private DocumentTransactionRepository  documentTransactionRepository;
//    @Override
//    public List<TransactionDocument> findAll(String keyword) {
//        return null;
//    }
//
//
//    @Override
//    public <T extends Transaction> void save(T transaction) {
//        documentTransactionRepository.save(transaction);
//    }
//}
