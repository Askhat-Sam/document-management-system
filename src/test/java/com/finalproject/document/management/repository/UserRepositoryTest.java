package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.User;
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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void test_findUserById() {
        // Given
        User userExpected = new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        userRepository.save(userExpected);
        // When
        User userActual = userRepository.findUserById(1L);

        // Then
        assertEquals("ivan", userActual.getFirstName());
        assertEquals(userExpected.getUserId(), userActual.getUserId());
    }

}