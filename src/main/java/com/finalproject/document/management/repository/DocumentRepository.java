package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUserId(Long id);
}
