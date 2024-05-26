package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
