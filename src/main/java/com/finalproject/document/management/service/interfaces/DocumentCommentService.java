package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.entity.DocumentComment;

import java.util.List;

public interface DocumentCommentService {
    List<DocumentCommentDTO> findAll();
    List<DocumentCommentDTO> findByUserId(String userId);
    List<DocumentCommentDTO> findAllByDocumentId(Long id);
    void update(DocumentComment documentComment);
}
