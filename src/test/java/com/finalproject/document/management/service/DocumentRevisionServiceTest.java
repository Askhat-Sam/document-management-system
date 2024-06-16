package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.DocumentRevisionDTO;
import com.finalproject.document.management.entity.DocumentRevision;
import com.finalproject.document.management.repository.DocumentRevisionRepository;
import com.finalproject.document.management.service.implementations.DocumentRevisionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class DocumentRevisionServiceTest {
    @Mock
    private DocumentRevision documentRevision;
    @Mock
    private DocumentRevisionDTO documentRevisionDTO;
    @InjectMocks
    DocumentRevisionServiceImpl documentRevisionServiceImpl;
    @Mock
    DocumentRevisionRepository documentRevisionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldFindAll() {
        // Given
        List<DocumentRevision> documentRevisionsList = new ArrayList<>();
        documentRevisionsList.add(new DocumentRevision(1L, "john.s", 1L, "2024-09-09",
                1L, "Validated", "New revision", "Susan.s"));
        documentRevisionsList.add(new DocumentRevision(2L, "susan.s", 1L, "2024-09-09",
                1L, "Awaiting validation", "New revision", "Susan.s"));
        when(documentRevisionRepository.findAll()).thenReturn(documentRevisionsList);

        // When
        List<DocumentRevisionDTO> documentRevisionsListActual = documentRevisionServiceImpl.findAll();

        // Then
        Assertions.assertEquals(2, documentRevisionsListActual.size());
        Assertions.assertEquals(documentRevisionsList.get(0).getUserId(), documentRevisionsListActual.get(0).getUserId());
        Assertions.assertEquals(documentRevisionsList.get(1).getStatus(), documentRevisionsListActual.get(1).getStatus());
    }

    @Test
    void shouldFindAllByDocumentId() {
        // Given
        List<DocumentRevision> documentRevisionsList = new ArrayList<>();
        documentRevisionsList.add(new DocumentRevision(1L, "john.s", 1L, "2024-09-09",
                1L, "Validated", "New revision", "Susan.s"));
        documentRevisionsList.add(new DocumentRevision(2L, "susan.s", 1L, "2024-09-09",
                1L, "Awaiting validation", "New revision", "Susan.s"));

        when(documentRevisionRepository.findAllByDocumentId(1L)).thenReturn(documentRevisionsList);

        // When
        List<DocumentRevisionDTO> documentRevisionsListActual = documentRevisionServiceImpl.findAllByDocumentId(1L);

        // Then
        Assertions.assertEquals(2, documentRevisionsListActual.size());
        Assertions.assertEquals("john.s", documentRevisionsListActual.get(0).getUserId());
        Assertions.assertEquals("Awaiting validation", documentRevisionsListActual.get(1).getStatus());
    }

    @Test
    void shouldFindByRevisionNumber() {
        // Given
        List<DocumentRevision> documentRevisionsList = new ArrayList<>();
        documentRevisionsList.add(new DocumentRevision(1L, "john.s", 1L, "2024-09-09",
                1L, "Validated", "New revision", "Susan.s"));
        documentRevisionsList.add(new DocumentRevision(3L, "andrey.s", 1L, "2024-09-09",
                3L, "Awaiting validation", "New revision", "John.s"));

        when(documentRevisionRepository.findByRevisionNumberAndDocumentId(1L, 1L)).thenReturn(documentRevisionsList.get(0));
        when(documentRevisionRepository.findByRevisionNumberAndDocumentId(3L, 1L)).thenReturn(documentRevisionsList.get(1));

        // When
        DocumentRevision documentRevisionActual1 = documentRevisionServiceImpl.findByRevisionNumberAndDocumentId(1L, 1L);
        DocumentRevision documentRevisionActual2 = documentRevisionServiceImpl.findByRevisionNumberAndDocumentId(3L, 1L);

        // Then
        Assertions.assertEquals("john.s", documentRevisionActual1.getUserId());
        Assertions.assertEquals("Validated", documentRevisionActual1.getStatus());

        Assertions.assertEquals("andrey.s", documentRevisionActual2.getUserId());
        Assertions.assertEquals("Awaiting validation", documentRevisionActual2.getStatus());
    }

}