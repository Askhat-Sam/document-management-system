package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.dto.DocumentRevisionDTO;
import com.finalproject.document.management.entity.DocumentRevision;
import com.finalproject.document.management.repository.DocumentRevisionRepository;
import com.finalproject.document.management.service.interfaces.DocumentRevisionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentRevisionServiceImpl implements DocumentRevisionService {
    private final DocumentRevisionRepository documentRevisionRepository;

    public DocumentRevisionServiceImpl(DocumentRevisionRepository documentRevisionRepository) {
        this.documentRevisionRepository = documentRevisionRepository;
    }


    @Override
    public List<DocumentRevisionDTO> findAll() {
        return documentRevisionRepository.findAll().stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DocumentRevisionDTO> findAllByDocumentId(Long id) {
        return documentRevisionRepository.findAllByDocumentId(id).stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    public DocumentRevisionDTO fromEntityToDTO(DocumentRevision documentRevision) {
        return new DocumentRevisionDTO(documentRevision.getId(), documentRevision.getUserId(),
                documentRevision.getDocumentId(), documentRevision.getDate(), documentRevision.getRevisionNumber(),
                documentRevision.getStatus(), documentRevision.getDescription(), documentRevision.getLink(),
                documentRevision.getValidatingUser());
    }

    public DocumentRevision findByRevisionNumberAndDocumentId(Long revisionNumber, Long documentId){
        return documentRevisionRepository.findByRevisionNumberAndDocumentId(revisionNumber, documentId);
    }

    @Override
    public void save(DocumentRevision documentRevision) {
        documentRevisionRepository.save(documentRevision);
    }

    @Override
    public DocumentRevision findById(Long id) {
        return documentRevisionRepository.findById(id).get();
    }
}
