package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentValidationRepository extends JpaRepository<DocumentValidation, Long> {
    List<DocumentValidation> findAllByUserId(String userId);
    Long countByStatusAndUserId(String status, String userId);
}
