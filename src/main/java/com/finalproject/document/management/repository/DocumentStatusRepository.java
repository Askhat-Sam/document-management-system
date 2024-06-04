package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, Long> {
}
