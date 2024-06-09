package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.entity.DocumentRevision;

import java.util.List;

public interface DocumentRevisionService {
    List<DocumentRevision> findAll();
    List<DocumentRevision> findAllByDocumentId(Long id);
}
