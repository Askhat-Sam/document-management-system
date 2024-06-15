package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.TransactionDocument;
import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.entity.TransactionUser;
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
public class DocumentTransactionRepositoryTest {
    @Autowired
    private DocumentTransactionRepository documentTransactionRepository;
    @Test
    public void test_findAllByUser() {
        // Given
        TransactionEntity transactionDocument1 = new TransactionDocument(1L, "2023-01-01","john.s", "POL-OO1",
                "New document has been created");
        TransactionEntity transactionDocument2 = new TransactionDocument(2L, "2021-01-08","susan.s", "INS-009",
                "New document has been updated");
        TransactionEntity transactionDocument3 = new TransactionDocument(3L, "2021-09-01","Marry.s", "MAN-021",
                "New document has been updated");
        TransactionEntity transactionDocument4 = new TransactionDocument(4L, "2022-01-01","john.s", "INS-009",
                "New document has been updated");

        documentTransactionRepository.save(transactionDocument1);
        documentTransactionRepository.save(transactionDocument2);
        documentTransactionRepository.save(transactionDocument3);
        documentTransactionRepository.save(transactionDocument4);

        // When
        List<TransactionEntity> documentTransactions1 = documentTransactionRepository.findAllByDocumentCode("POL-OO1");
        List<TransactionEntity> documentTransactions2 = documentTransactionRepository.findAllByDocumentCode("INS-009");
        List<TransactionEntity> documentTransactions3 = documentTransactionRepository.findAllByDocumentCode("MAN-021");

        // Then
        assertEquals(1,documentTransactions1.size());
        assertEquals(2,documentTransactions2.size());
        assertEquals(1,documentTransactions3.size());
    }

}