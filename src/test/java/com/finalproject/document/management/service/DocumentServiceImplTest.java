package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.DocumentRepository;
import com.finalproject.document.management.service.implementations.DocumentServiceImpl;
import com.finalproject.document.management.service.interfaces.DocumentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentServiceImplTest {
    @Mock
    private Document document;
    @Mock
    private DocumentDTO documentDTO;
    @InjectMocks
    DocumentServiceImpl documentServiceImpl;
    @Mock
    DocumentRepository documentRepository;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void test_findAll() {
        // Given
        List<Document> documentListList = new ArrayList<>();
        documentListList.add(new Document("POL-001", "Policy", "Safety policy", 1L,
                "Validated", "2024-09-01", "2024-09-05", "john.s"));
        documentListList.add(new Document("MAN-001", "Manual", "IT manual", 2L,
                "In progress", "2023-01-01", "2023-02-05", "susan.s"));
        documentListList.add(new Document("INS-003", "Instruction", "Working instruction", 3L,
                "Validated", "2024-01-01", "2024-02-05", "andrey.s"));
        when(documentRepository.findAll()).thenReturn(documentListList);

        // When
        List<DocumentDTO> documentDTOList = documentServiceImpl.findAll();

        // Then
        Assertions.assertEquals(3, documentDTOList.size());
        Assertions.assertEquals("Policy", documentDTOList.get(0).getDocumentType());
        Assertions.assertEquals("IT manual", documentDTOList.get(1).getName());
        Assertions.assertEquals("In progress", documentDTOList.get(1).getStatus());
        Assertions.assertEquals("Validated", documentDTOList.get(0).getStatus());
        Assertions.assertEquals("susan.s", documentDTOList.get(1).getAuthor());
        Assertions.assertEquals("2024-01-01", documentDTOList.get(2).getCreationDate());
    }

    @Test
    public void test_fromEntityToDTO() {
        // Given
        when(document.getId()).thenReturn(1L);
        when(document.getDocumentCode()).thenReturn("POL-001");
        when(document.getDocumentType()).thenReturn("Policy");
        when(document.getName()).thenReturn("Safety policy");
        when(document.getStatus()).thenReturn("Validated");
        when(document.getCreationDate()).thenReturn("2024-09-01");
        when(document.getModificationDate()).thenReturn("2024-09-05");
        when(document.getRevisionNumber()).thenReturn(1L);
        when(document.getAuthor()).thenReturn("john.s");

        // When
        DocumentDTO documentDTO = documentServiceImpl.fromEntityToDTO(document);

        // Then
        Assertions.assertEquals((Long) 1L, documentDTO.getId());
        Assertions.assertEquals("POL-001", documentDTO.getDocumentCode());
        Assertions.assertEquals("Policy", documentDTO.getDocumentType());
        Assertions.assertEquals("Safety policy", documentDTO.getName());
        Assertions.assertEquals("Validated", documentDTO.getStatus());
        Assertions.assertEquals("2024-09-01", documentDTO.getCreationDate());
        Assertions.assertEquals("2024-09-05", documentDTO.getModificationDate());
        Assertions.assertEquals(1L, documentDTO.getRevisionNumber());
        Assertions.assertEquals("john.s", documentDTO.getAuthor());
    }

    @Test
    public void test_fromDTOToEntity() {
        // Given
        when(documentDTO.getId()).thenReturn(1L);
        when(documentDTO.getDocumentCode()).thenReturn("POL-001");
        when(documentDTO.getDocumentType()).thenReturn("Policy");
        when(documentDTO.getName()).thenReturn("Safety policy");
        when(documentDTO.getStatus()).thenReturn("Validated");
        when(documentDTO.getCreationDate()).thenReturn("2024-09-01");
        when(documentDTO.getModificationDate()).thenReturn("2024-09-05");
        when(documentDTO.getRevisionNumber()).thenReturn(1L);
        when(documentDTO.getAuthor()).thenReturn("john.s");

        // When
        Document document = documentServiceImpl.convertToEntity(documentDTO);

        // Then
        Assertions.assertEquals((Long) 1L, document.getId());
        Assertions.assertEquals("POL-001", document.getDocumentCode());
        Assertions.assertEquals("Policy", document.getDocumentType());
        Assertions.assertEquals("Safety policy", document.getName());
        Assertions.assertEquals("Validated", document.getStatus());
        Assertions.assertEquals("2024-09-01", document.getCreationDate());
        Assertions.assertEquals("2024-09-05", document.getModificationDate());
        Assertions.assertEquals(1L, document.getRevisionNumber());
        Assertions.assertEquals("john.s", document.getAuthor());
    }

    @Test
    public void test_findById() {
        // Given
        Document documentExpected= new Document(1L, "POL-001", "Policy", "Safety policy", 1L,
                "Validated", "2024-09-01", "2024-09-05", "john.s");
        when(documentRepository.findById(1L)).thenReturn(Optional.of(documentExpected));

        // When
        DocumentDTO documentActual = documentServiceImpl.findById(1L);

        // Then
        Assertions.assertNotNull(documentActual); // Ensure document is not null
        Assertions.assertEquals(documentActual.getDocumentCode(), documentExpected.getDocumentCode());
        Assertions.assertEquals("Safety policy", documentActual.getName());
        Assertions.assertEquals("john.s", documentActual.getAuthor());
    }

    @Test
    public void testUpdate() {
        // Given
        Document documentToUpdate = new Document();
        when(documentRepository.save(documentToUpdate)).thenReturn(documentToUpdate);

        // When
        documentServiceImpl.update(documentToUpdate);

        // Then
        verify(documentRepository).save(documentToUpdate); // Verify that documentRepository.save() is called with the document object
    }

    @Test
    public void test_findAllDocumentsByUserIds() {
        // Given
        List<Document> expectedDocuments =new ArrayList<>();
        expectedDocuments.add(new Document(1L,"POL-001", "Policy", "Safety policy", 1L,
                "Validated", "2024-09-01", "2024-09-05", "john.s"));
        expectedDocuments.add(new Document(2L,"MAN-001", "Manual", "IT manual", 2L,
                "In progress", "2023-01-01", "2023-02-05", "john.s"));
        expectedDocuments.add(new Document(3L,"INS-003", "Instruction", "Working instruction", 3L,
                "Validated", "2024-01-01", "2024-02-05", "john.s"));
        when(documentRepository.findByUserId("john.s")).thenReturn(expectedDocuments);

        // When
        List<DocumentDTO> actualDocuments = documentServiceImpl.findByUserId("john.s");
        List<Document> actualDocumentsMapped = actualDocuments.stream()
                .map(d->documentServiceImpl.convertToEntity(d))
                .toList();

        // Then
        Assertions.assertEquals(expectedDocuments.size(), actualDocumentsMapped.size());
        for (int i = 0; i < expectedDocuments.size(); i++) {
            Assertions.assertEquals(expectedDocuments.get(i).getDocumentCode(), actualDocumentsMapped.get(i).getDocumentCode());
            Assertions.assertEquals(expectedDocuments.get(i).getAuthor(), actualDocumentsMapped.get(i).getAuthor());
        }
    }



}