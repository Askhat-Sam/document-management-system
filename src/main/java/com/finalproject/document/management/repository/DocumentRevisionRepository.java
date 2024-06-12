package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRevisionRepository extends JpaRepository<DocumentRevision, Long> {
    List<DocumentRevision> findAllByDocumentId(Long id);
    DocumentRevision findByRevisionNumber(Long revisionNumber);
}
