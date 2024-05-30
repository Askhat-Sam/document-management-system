package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;

import java.util.List;

public interface DocumentService {
    List<Document> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);

    Document findById(int id);

    void update(Document document);

    void deleteDocumentById(int id);

    String uploadDocument(String path, String actionType);
}
