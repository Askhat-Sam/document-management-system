package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.DocumentStatus;
import com.finalproject.document.management.repository.DocumentStatusRepository;

public class DocumentStatusServiceImpl implements DocumentStatusService{
    DocumentStatusRepository documentStatusRepository;

    public DocumentStatusServiceImpl(DocumentStatusRepository documentStatusRepository) {
        this.documentStatusRepository = documentStatusRepository;
    }

    @Override
    public DocumentStatus findByID(int id) {
        return documentStatusRepository.getById(id);
    }
}
