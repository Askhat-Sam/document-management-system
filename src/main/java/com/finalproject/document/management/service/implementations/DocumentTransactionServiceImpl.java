package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.repository.DocumentTransactionRepository;
import com.finalproject.document.management.service.interfaces.DocumentTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTransactionServiceImpl implements DocumentTransactionService {

    private final DocumentTransactionRepository documentTransactionRepository;

    public DocumentTransactionServiceImpl(DocumentTransactionRepository documentTransactionRepository) {
        this.documentTransactionRepository = documentTransactionRepository;
    }

    @Override
    public List<TransactionEntity> findAll(String keyword) {
        return null;
    }


    @Override
    public List<TransactionEntity> findAllByDocument(Long documentId) {
        return null;
    }

    @Override
    public void save(TransactionEntity transaction) {
        documentTransactionRepository.save(transaction);
    }

    @Override
    public List<TransactionEntity> findAllByUser(String userId) {
        return null;
    }


    public List<TransactionEntity> findAllByDocumentId(Long documentId) {
        return documentTransactionRepository.findAllByDocumentId(documentId);
    }
}