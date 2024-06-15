package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.service.interfaces.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class DocumentServiceTest {
    @Autowired
    private DocumentService documentService;
    @Test
    public void test_findById() {
        // Given
        Document documentExpected = new Document("POL-001", "Policy", "Safety policy", 1L,
                "Validated", "2024-09-01", "2024-09-05", "john.s");
        documentService.update(documentExpected);
        // When
        DocumentDTO documentActual = documentService.findById(1L);

        // Then
        assertEquals(documentExpected.getDocumentCode(), documentActual.getDocumentCode());
        assertEquals(documentExpected.getAuthor(), documentActual.getAuthor());
        assertEquals(documentExpected.getName(), documentActual.getName());
    }
}