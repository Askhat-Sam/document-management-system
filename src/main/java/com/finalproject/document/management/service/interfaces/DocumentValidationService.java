package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.DocumentValidationDTO;
import com.finalproject.document.management.entity.DocumentValidation;

import java.util.List;

public interface DocumentValidationService {
    List<DocumentValidationDTO> findAll();
    List<DocumentValidationDTO> findAllByUserId(String userId);
    DocumentValidation save(DocumentValidation documentValidation);
    DocumentValidationDTO findById(Long id);
}
