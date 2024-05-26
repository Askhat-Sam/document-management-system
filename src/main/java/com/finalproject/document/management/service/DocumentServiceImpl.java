package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private DocumentRepository documentRepository;

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }
}
