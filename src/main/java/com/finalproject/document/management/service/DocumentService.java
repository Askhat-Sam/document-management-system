package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> findAll();
    Document findById(int id);
    void update(Document document);
    void deleteDocumentById(int id);
}
