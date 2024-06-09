package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.DocumentComment;
import com.finalproject.document.management.repository.DocumentCommentRepository;
import com.finalproject.document.management.service.interfaces.DocumentCommentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DocumentCommentServiceImpl implements DocumentCommentService {
    private DocumentCommentRepository documentCommentRepository;

    public DocumentCommentServiceImpl(DocumentCommentRepository documentCommentRepository) {
        this.documentCommentRepository = documentCommentRepository;
    }

    @Override
    public List<DocumentComment> findAll() {
        return documentCommentRepository.findAll();
    }

    @Override
    public List<DocumentComment> findByUserId(Long id) {
        return documentCommentRepository.findByUserId(id);
    }
}