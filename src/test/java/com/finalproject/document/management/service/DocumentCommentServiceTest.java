package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.DocumentComment;
import com.finalproject.document.management.repository.DocumentCommentRepository;
import com.finalproject.document.management.service.implementations.DocumentCommentServiceImpl;
import com.finalproject.document.management.service.interfaces.DocumentCommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class DocumentCommentServiceTest {
    @Mock
    private DocumentComment documentComment;
    @Mock
    private DocumentCommentDTO documentCommentDTO;
    @InjectMocks
    DocumentCommentServiceImpl documentCommentServiceImpl;
    @Mock
    DocumentCommentRepository documentCommentRepository;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void findAll() {
        // Given
        List<DocumentComment> documentCommentsList = new ArrayList<>();
        documentCommentsList.add(new DocumentComment(1L, "john.s", "2024-09-02","New comment to document"));
        documentCommentsList.add(new DocumentComment(2L, "susan.s", "2024-09-01","New comment to document 2"));
        when(documentCommentRepository.findAll()).thenReturn(documentCommentsList);

        // When
        List<DocumentCommentDTO> documentCommentsListActual = documentCommentServiceImpl.findAll();

        // Then
        Assertions.assertEquals(2, documentCommentsListActual.size());
        Assertions.assertEquals(documentCommentsList.get(0).getDocumentId(), documentCommentsListActual.get(0).getDocumentId());
        Assertions.assertEquals(documentCommentsList.get(1).getUserId(), documentCommentsListActual.get(1).getUserId());
    }

    @Test
    void findAllByDocumentId() {
        // Given
        List<DocumentComment> documentCommentsList = new ArrayList<>();
        documentCommentsList.add(new DocumentComment(2L, "john.s", "2024-09-02","New comment to document"));
        documentCommentsList.add(new DocumentComment(2L, "susan.s", "2024-09-01","New comment to document 2"));
        documentCommentsList.add(new DocumentComment(2L, "susan.s", "2024-09-01","New comment to document 2"));
        documentCommentsList.add(new DocumentComment(2L, "susan.s", "2024-09-01","New comment to document 2"));
        when(documentCommentRepository.findAllByDocumentId(2L)).thenReturn(documentCommentsList);

        // When
        List<DocumentCommentDTO> documentCommentsListActual = documentCommentServiceImpl.findAllByDocumentId(2L);

        // Then
        Assertions.assertEquals(4, documentCommentsListActual.size());
        Assertions.assertEquals(documentCommentsList.get(0).getDocumentId(), documentCommentsListActual.get(0).getDocumentId());
        Assertions.assertEquals(documentCommentsList.get(1).getUserId(), documentCommentsListActual.get(1).getUserId());
    }
}