package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentRevision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:drop the test DB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentRevisionRepositoryTest {
    @Autowired
    private DocumentRevisionRepository documentRevisionRepository;

    @Test
    public void shouldFindById() {
        // Given
        DocumentRevision documentRevisionExpected = new DocumentRevision(1L, "john.s", 1L, "2024-09-09",
                2L, "Validated", "New revision", "Susan.s");
        documentRevisionRepository.save(documentRevisionExpected);
        // When
        DocumentRevision documentRevisionActual = documentRevisionRepository.findById(1L).get();

        // Then
        assertEquals(documentRevisionExpected.getDocumentId(), documentRevisionActual.getDocumentId());
        assertEquals(documentRevisionExpected.getStatus(), documentRevisionActual.getStatus());
    }

    @Test
    public void shouldFindByRevisionNumber() {
        // Given
        Long revisionNumber = 5L;
        DocumentRevision documentRevisionExpected = new DocumentRevision(1L, "john.s", 1L, "2024-09-09",
                revisionNumber, "Validated", "New revision", "Susan.s");
        documentRevisionRepository.save(documentRevisionExpected);

        // When
        DocumentRevision documentRevisionActual = documentRevisionRepository.findByRevisionNumberAndDocumentId(revisionNumber, 1L);

        // Then
        assertEquals(revisionNumber, documentRevisionActual.getRevisionNumber());
        assertEquals(1L, documentRevisionActual.getDocumentId());
    }
}