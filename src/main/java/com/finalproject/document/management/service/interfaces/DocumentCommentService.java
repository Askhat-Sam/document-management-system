package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.entity.DocumentComment;

import java.util.List;

public interface DocumentCommentService {
    List<DocumentComment> findAll();
    List<DocumentComment> findByUserId(Long id);
}
