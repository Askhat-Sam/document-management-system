package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
