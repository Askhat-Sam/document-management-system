package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;
    @Test
    public void testFindUserById() {
        // Given
        User userExpected = new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        userService.update(userExpected);
        // When
        User userActual = userService.findUserById(1L);

        // Then
        assertEquals("ivan", userActual.getFirstName());
        assertEquals(userExpected.getDepartment(), userActual.getDepartment());
    }
}