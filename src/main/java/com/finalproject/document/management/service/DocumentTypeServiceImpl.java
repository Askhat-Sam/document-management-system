package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.DocumentType;
import com.finalproject.document.management.repository.DocumentTypeRepository;

public class DocumentTypeServiceImpl implements DocumentTypeService{
    private DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public DocumentType findById(int id) {
        return documentTypeRepository.getById(id);
    }
}
