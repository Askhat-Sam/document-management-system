package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.entity.DocumentComment;
import com.finalproject.document.management.repository.DocumentCommentRepository;
import com.finalproject.document.management.service.interfaces.DocumentCommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentCommentServiceImpl implements DocumentCommentService {
    private DocumentCommentRepository documentCommentRepository;

    public DocumentCommentServiceImpl(DocumentCommentRepository documentCommentRepository) {
        this.documentCommentRepository = documentCommentRepository;
    }

    @Override
    public List<DocumentCommentDTO> findAll() {
        return documentCommentRepository.findAll().stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }
    public DocumentCommentDTO fromEntityToDTO(DocumentComment documentComment) {
        return new DocumentCommentDTO(documentComment.getId(), documentComment.getDocumentId(),
                documentComment.getUserId(), documentComment.getDate(), documentComment.getComment());
    }


    @Override
    public List<DocumentCommentDTO> findByUserId(String userid) {

        return documentCommentRepository.findByUserId(userid).stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DocumentCommentDTO> findAllByDocumentId(Long id) {
        return documentCommentRepository.findAllByDocumentId(id).stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }
}
