package com.finalproject.document.management.repository;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.entity.DocumentComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentCommentRepository extends JpaRepository<DocumentComment, Long> {
    List<DocumentComment> findByUserId(String userId);

    List<DocumentComment> findAllByDocumentId(Long id);
}
