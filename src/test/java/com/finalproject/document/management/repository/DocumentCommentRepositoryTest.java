package com.finalproject.document.management.repository;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.DocumentComment;
import com.finalproject.document.management.service.interfaces.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:drop the test DB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentCommentRepositoryTest {
    @Autowired
    private DocumentCommentRepository documentCommentRepository;
    @Test
    public void test_findById() {
        // Given
        DocumentComment documentCommentExpected = new DocumentComment(1L, "john.s", "2024-09-02","New comment to document");
        documentCommentRepository.save(documentCommentExpected);
        // When
        DocumentComment documentCommentActual = documentCommentRepository.findById(1L).get();

        // Then
        assertEquals(documentCommentExpected.getComment(), documentCommentActual.getComment());
        assertEquals(documentCommentExpected.getId(), documentCommentActual.getId());
    }

    @Test
    public void test_findAllByDocumentId() {
        // Given
        Long documentId = 1L;
        DocumentComment documentCommentExpected = new DocumentComment(1L,  documentId,"john.s", "2024-09-02", "New comment to document");
        DocumentComment documentCommentExpected2 = new DocumentComment(2L,  documentId,"susan.s", "2024-01-02", "New comment to document 2");
        documentCommentRepository.save(documentCommentExpected);
        documentCommentRepository.save(documentCommentExpected2);
        // When
        List<DocumentComment> documentCommentActual = documentCommentRepository.findAllByDocumentId(documentId);

        // Then
        assertEquals(2, documentCommentActual.size());
        assertEquals("susan.s", documentCommentActual.get(1).getUserId());
        assertEquals("2024-09-02", documentCommentActual.get(0).getDate());
    }
}