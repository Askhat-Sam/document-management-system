package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentValidation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:drop the test DB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentValidationRepositoryTest {
    @Autowired
    private DocumentValidationRepository documentValidationRepository;
    @Test
    public void test_findAllByUserId() {
        // Given
        DocumentValidation documentValidation1 = new DocumentValidation(1L, "POL-001","Safety policy", 1L,
                "john.s", "Awaiting validation");
        DocumentValidation documentValidation2 = new DocumentValidation(2L, "MAN-001","Management manual", 4L,
                "susan.s", "Awaiting validation");
        DocumentValidation documentValidation3 = new DocumentValidation(3L, "INS-001","Planning instruction", 3L,
                "marry.s", "Awaiting validation");
        DocumentValidation documentValidation4 = new DocumentValidation(4L, "MAN-003","Documentation manual", 1L,
                "john.s", "Awaiting validation");
        documentValidationRepository.save(documentValidation1);
        documentValidationRepository.save(documentValidation2);
        documentValidationRepository.save(documentValidation3);
        documentValidationRepository.save(documentValidation4);

        // When
        List<DocumentValidation> documentValidationListActual1 = documentValidationRepository.findAllByUserId("john.s");
        List<DocumentValidation> documentValidationListActual2 = documentValidationRepository.findAllByUserId("susan.s");

        // Then
        assertEquals(2,documentValidationListActual1.size());
        assertEquals(documentValidation1.getDocumentCode(),documentValidationListActual1.get(0).getDocumentCode());
        assertEquals(1,documentValidationListActual2.size());
        assertEquals(documentValidation2.getName(),documentValidationListActual2.get(0).getName());
    }

    @Test
    public void test_countByStatusAndUserId() {
        // Given
        DocumentValidation documentValidation1 = new DocumentValidation(1L, "POL-001","Safety policy", 1L,
                "john.ss", "Awaiting validation");
        DocumentValidation documentValidation2 = new DocumentValidation(2L, "MAN-001","Management manual", 4L,
                "susan.ss", "Awaiting validation");
        DocumentValidation documentValidation3 = new DocumentValidation(3L, "INS-001","Planning instruction", 3L,
                "marry.ss", "Awaiting validation");
        DocumentValidation documentValidation4 = new DocumentValidation(4L, "MAN-004","Documentation manual", 6L,
                "john.ss", "Awaiting validation");
        DocumentValidation documentValidation5= new DocumentValidation(5L, "INS-004","Planning instruction", 3L,
                "marry.ss", "Awaiting validation");
        DocumentValidation documentValidation6 = new DocumentValidation(6L, "MAN-006","Documentation manual", 8L,
                "john.ss", "Validated");
        DocumentValidation documentValidation7= new DocumentValidation(7L, "INS-09","Planning instruction", 3L,
                "marry.ss", "Validated");
        documentValidationRepository.save(documentValidation1);
        documentValidationRepository.save(documentValidation2);
        documentValidationRepository.save(documentValidation3);
        documentValidationRepository.save(documentValidation4);
        documentValidationRepository.save(documentValidation5);
        documentValidationRepository.save(documentValidation6);
        documentValidationRepository.save(documentValidation7);

        // When
        Long documentValidationsCount1 = documentValidationRepository.countByStatusAndUserId("Validated", "john.ss");
        Long documentValidationsCount2 = documentValidationRepository.countByStatusAndUserId("Awaiting validation", "john.ss");
        Long documentValidationsCount3 = documentValidationRepository.countByStatusAndUserId("Awaiting validation", "susan.ss");
        Long documentValidationsCount4 = documentValidationRepository.countByStatusAndUserId("Validated", "susan.ss");
        Long documentValidationsCount5 = documentValidationRepository.countByStatusAndUserId("Awaiting validation", "marry.ss");
        Long documentValidationsCount6 = documentValidationRepository.countByStatusAndUserId("Validated", "marry.ss");

        // Then
        assertEquals(1,documentValidationsCount1);
        assertEquals(2,documentValidationsCount2);
        assertEquals(1,documentValidationsCount3);
        assertEquals(0,documentValidationsCount4);
        assertEquals(2,documentValidationsCount5);
        assertEquals(1,documentValidationsCount6);
    }
}