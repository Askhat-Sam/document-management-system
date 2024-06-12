package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.dto.DocumentValidationDTO;
import com.finalproject.document.management.entity.DocumentValidation;
import com.finalproject.document.management.repository.DocumentValidationRepository;
import com.finalproject.document.management.service.interfaces.DocumentValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentValidationServiceImpl implements DocumentValidationService {
    private final DocumentValidationRepository documentValidationRepository;
    @Override
    public List<DocumentValidationDTO> findAll() {
        return documentValidationRepository.findAll().stream().map(this::fromEntityToDTO).collect(Collectors.toList());
    }
    @Override
    public List<DocumentValidationDTO> findAllByUserId(String userId) {
        return documentValidationRepository.findAllByUserId(userId).stream().map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public DocumentValidation save(DocumentValidation documentValidation) {
        return documentValidationRepository.save(documentValidation);
    }

    @Override
    public DocumentValidationDTO findById(Long id) {
       DocumentValidation documentValidation = documentValidationRepository.findById(id).get();
        return new DocumentValidationDTO(documentValidation.getId(), documentValidation.getDocumentCode(), documentValidation.getName(),
                documentValidation.getRevisionNumber(), documentValidation.getUserId(), documentValidation.getStatus());
    }

    public DocumentValidationDTO fromEntityToDTO(DocumentValidation documentValidation) {
        return new DocumentValidationDTO(documentValidation.getId(), documentValidation.getDocumentCode(),
                documentValidation.getName(),documentValidation.getRevisionNumber(),documentValidation.getUserId(),
                documentValidation.getStatus());
    }

    public static DocumentValidation fromDTOToEntity(DocumentValidationDTO documentValidationDTO) {
        return new DocumentValidation(documentValidationDTO.getId(), documentValidationDTO.getDocumentCode(),
                documentValidationDTO.getName(), documentValidationDTO.getRevisionNumber(), documentValidationDTO.getUserId(),
                documentValidationDTO.getStatus());
    }
}
