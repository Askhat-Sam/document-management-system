package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.DocumentComment;

import java.util.List;

public interface DocumentCommentService {
    List<DocumentComment> findByUserId(Long id);
}