package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.DocumentValidation;
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
public class UserTransactionRepositoryTest {
    @Autowired
    private UserTransactionRepository userTransactionRepository;
    @Test
    public void test_findAllByUser() {
        // Given
        TransactionEntity transaction1 = new TransactionUser(1L, "2024-09-07","john.s", "marry.s",
                "New user has been created");
        TransactionEntity transaction2 = new TransactionUser(2L, "2020-09-07","john.s", "susan.s",
                "New user has been created");
        TransactionEntity transaction3 = new TransactionUser(3L, "2024-06-07","andrey.s", "john.s",
                "New user has been created");
        TransactionEntity transaction4 = new TransactionUser(4L, "2022-01-17","susan.s", "andrey.s",
                "New user has been created");
        TransactionEntity transaction5 = new TransactionUser(5L, "2025-11-17","susan.s", "john.s",
                "New user has been created");
        TransactionEntity transaction6 = new TransactionUser(6L, "2022-03-17","susan.s", "marry.s",
                "New user has been created");
        userTransactionRepository.save(transaction1);
        userTransactionRepository.save(transaction2);
        userTransactionRepository.save(transaction3);
        userTransactionRepository.save(transaction4);
        userTransactionRepository.save(transaction5);
        userTransactionRepository.save(transaction6);

        // When
        List<TransactionEntity> userTransactionList1 = userTransactionRepository.findAllByUser("susan.s");
        List<TransactionEntity> userTransactionList2 = userTransactionRepository.findAllByUser("john.s");
        List<TransactionEntity> userTransactionList3 = userTransactionRepository.findAllByUser("andrey.s");

        // Then
        assertEquals(3,userTransactionList1.size());
        assertEquals(2,userTransactionList2.size());
        assertEquals(1,userTransactionList3.size());
        assertEquals(transaction6.getClass().getName(),userTransactionList3.get(0).getClass().getName());
    }

}