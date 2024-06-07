package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.DocumentRevision;
import com.finalproject.document.management.repository.DocumentRevisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DocumentRevisionServiceImpl implements DocumentRevisionService{
    private final DocumentRevisionRepository documentRevisionRepository;

    public DocumentRevisionServiceImpl(DocumentRevisionRepository documentRevisionRepository) {
        this.documentRevisionRepository = documentRevisionRepository;
    }


    @Override
    public List<DocumentRevision> findAll() {
        return documentRevisionRepository.findAll();
    }

    @Override
    public List<DocumentRevision> findAllByDocumentId(Long id) {
        return documentRevisionRepository.findAllByDocumentId(id);
    }
}
