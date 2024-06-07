package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DocumentService {
    List<Document> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);
    List<Document> findAll();

    Document findById(Long id);

    void update(Document document);

    void deleteDocumentById(Long id);

    String uploadDocument(String fileName, String path, String actionType);

    ResponseEntity<byte[]> downloadListAsExcel();
    List<Document> findByUserId(Long id);
}
