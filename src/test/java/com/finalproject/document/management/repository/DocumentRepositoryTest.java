package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:drop the test DB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentRepositoryTest {
    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void shouldFindById() {
        // Given
        Document documentExpected = new Document("POL-001", "Policy", "Safety policy", 1L,
                "Validated", "2024-09-01", "2024-09-05", "john.s");
        documentRepository.save(documentExpected);
        // When
        Optional<Document> documentActual = documentRepository.findById(1L);

        // Then
        assertEquals(documentExpected.getDocumentCode(), documentActual.get().getDocumentCode());
        assertEquals(documentExpected.getAuthor(), documentActual.get().getAuthor());
        assertEquals(documentExpected.getName(), documentActual.get().getName());
    }
}