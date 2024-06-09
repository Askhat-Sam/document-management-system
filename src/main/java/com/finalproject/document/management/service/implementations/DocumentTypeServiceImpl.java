package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.DocumentType;
import com.finalproject.document.management.repository.DocumentTypeRepository;
import com.finalproject.document.management.service.interfaces.DocumentTypeService;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public DocumentType findById(Long id) {
        return documentTypeRepository.getById(id);
    }
}