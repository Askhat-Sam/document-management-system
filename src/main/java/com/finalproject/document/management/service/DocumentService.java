package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;

import java.util.List;

public interface DocumentService {
    List<Document> findAll();
}
