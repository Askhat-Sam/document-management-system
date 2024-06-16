package com.finalproject.document.management.controller;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.service.interfaces.DocumentService;
import com.finalproject.document.management.service.interfaces.DocumentValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentValidationService documentValidationService;

    @Mock
    private Model model;

    @InjectMocks
    private DocumentController documentController;

    @Test
    public void shouldGetDocuments() {
        // Given
        List<DocumentDTO> mockedDocuments = Arrays.asList(new DocumentDTO(), new DocumentDTO());
        String loggedUser = "testUser";
        long mockedCountAwaitingValidation = 2L;

        when(documentService.findAll(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString())).thenReturn(mockedDocuments);
        when(documentValidationService.countByStatusAndUserId(anyString(), anyString())).thenReturn(mockedCountAwaitingValidation);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(loggedUser);

        // When
        String result = documentController.getDocuments(1, 10, "name", "asc", "keyword", "column", model);

        // Then
        verify(documentService).findAll(1, 10, "name", "asc", "keyword", "column");
        verify(documentValidationService).countByStatusAndUserId("Awaiting validation", loggedUser);
        verify(model).addAttribute("documents", mockedDocuments);
        verify(model).addAttribute("countAwaitingValidation", mockedCountAwaitingValidation);
        assertEquals("documents/documents", result);
    }
}