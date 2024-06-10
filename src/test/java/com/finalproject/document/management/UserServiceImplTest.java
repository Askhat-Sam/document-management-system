package com.finalproject.document.management;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.UserRepository;
import com.finalproject.document.management.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // given
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active"));
        userList.add(new User(1L, "sergey.s", "sergey", "sergeyev",
                "sergey.s@example.com", "Procurement", "ROLE_MANAGER", "Not active"));

        // when
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDTO> userDTOList = userService.findAll();

        // then
        assertEquals(2, userDTOList.size());
        assertEquals("ivan", userDTOList.get(0).getFirstName());
        assertEquals("ROLE_MANAGER", userDTOList.get(1).getRole());
        assertEquals("Procurement", userDTOList.get(1).getDepartment());
        assertEquals("Active", userDTOList.get(0).getStatus());
        assertEquals("sergey.s@example.com", userDTOList.get(1).getEmail());
        assertEquals("ivanov", userDTOList.get(0).getLastName());
    }

    @Test
    public void testSave() {
        // Given
        User user = new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");

        // When
        userService.save(user);

        // Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindAllUserIds() {
        // Given
        List<User> expectedUserIds =new ArrayList<>();
        User user1 = new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        User user2 = new User(1L, "sergey.s", "sergey", "sergeyev",
                "sergey.s@example.com", "Procurement", "ROLE_MANAGER", "Not active");
        User user3 = new User(1L, "nikolay.in", "nikolay", "nikolayev",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        expectedUserIds.add(user1);
        expectedUserIds.add(user2);
        expectedUserIds.add(user3);

        // When
        List<String> expectedUserIdsMapped = expectedUserIds.stream().map(User::getUserId).toList();
        when(userRepository.findAllUserIds()).thenReturn(expectedUserIdsMapped);
        List<String> actualUserIds = userService.findAllUserIds();

        // Then
        assertEquals(expectedUserIdsMapped.size(), actualUserIds.size());
        for (int i = 0; i < expectedUserIdsMapped.size(); i++) {
            assertEquals(expectedUserIdsMapped.get(i), actualUserIds.get(i));
        }
    }

    @Test
    public void testFindById() {
        // Given
        Long id = 1L;
        User expectedUser = new User(id, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // When
        User actualUser = userService.findById(id);

        // Then
        assertEquals(expectedUser, actualUser);
        assertThrows(RuntimeException.class, () -> userService.findById(2L)); //check for non existing user with id 2
    }

    // Add more test cases for other methods as needed
}