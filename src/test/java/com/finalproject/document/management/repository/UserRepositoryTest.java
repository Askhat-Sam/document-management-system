package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.implementations.UserServiceImpl;
import com.finalproject.document.management.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void testFindById() {
        // Given
        User expectedUser = new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        // Call the method you want to test
        Optional<User> result = userRepository.findById(1L);

        // Assert that the returned result is as expected
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

}