package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> findAll();
    Optional<Document> findById(int id);
}
