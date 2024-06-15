package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.DocumentRevisionDTO;
import com.finalproject.document.management.entity.DocumentRevision;

import java.util.List;

public interface DocumentRevisionService {
    List<DocumentRevisionDTO> findAll();
    List<DocumentRevisionDTO> findAllByDocumentId(Long id);
    DocumentRevision findByRevisionNumberAndDocumentId(Long revisionNumber, Long documentId);
    void save(DocumentRevision documentRevision);

    DocumentRevision findById(Long id);
}
