package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.LongToIntFunction;

public interface DocumentValidationRepository extends JpaRepository<DocumentValidation, Long> {
    List<DocumentValidation> findAllByUserId(String userId);
}
