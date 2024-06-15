package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.UserRepository;
import com.finalproject.document.management.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private User user;

    @Mock
    private UserDTO userDTO;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_findAll() {
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
        Assertions.assertEquals(2, userDTOList.size());
        Assertions.assertEquals("ivan", userDTOList.get(0).getFirstName());
        Assertions.assertEquals("ROLE_MANAGER", userDTOList.get(1).getRole());
        Assertions.assertEquals("Procurement", userDTOList.get(1).getDepartment());
        Assertions.assertEquals("Active", userDTOList.get(0).getStatus());
        Assertions.assertEquals("sergey.s@example.com", userDTOList.get(1).getEmail());
        Assertions.assertEquals("ivanov", userDTOList.get(0).getLastName());
    }

    @Test
    public void test_findAllUserIds() {
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
        List<String> expectedUserIdsMapped = expectedUserIds.stream().map(User::getUserId).toList();
        when(userRepository.findAllUserIds()).thenReturn(expectedUserIdsMapped);

        // When
        List<String> actualUserIds = userService.findAllUserIds();

        // Then
        Assertions.assertEquals(expectedUserIdsMapped.size(), actualUserIds.size());
        for (int i = 0; i < expectedUserIdsMapped.size(); i++) {
            Assertions.assertEquals(expectedUserIdsMapped.get(i), actualUserIds.get(i));
        }
    }

    @Test
    public void test_findById() {
        // Given
        User userExpected= new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userExpected));

        // When
        UserDTO userActual = userService.findById(1L);

        // Then
        Assertions.assertNotNull(userActual); // Ensure user is not null
        Assertions.assertEquals(userActual.getUserId(), userExpected.getUserId());
        Assertions.assertEquals("ivan", userActual.getFirstName());
    }

    @Test
    public void test_fromEntityToDTO() {
        // Given
        when(user.getId()).thenReturn(1L);
        when(user.getUserId()).thenReturn("user123");
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getEmail()).thenReturn("john.doe@example.com");
        when(user.getDepartment()).thenReturn("IT");
        when(user.getRole()).thenReturn("Engineer");
        when(user.getStatus()).thenReturn("Active");

        // When
        UserDTO userDTO = userServiceImpl.fromEntityToDTO(user);

        // Then
        Assertions.assertEquals((Long) 1L, userDTO.getId());
        Assertions.assertEquals("user123", userDTO.getUserId());
        Assertions.assertEquals("John", userDTO.getFirstName());
        Assertions.assertEquals("Doe", userDTO.getLastName());
        Assertions.assertEquals("john.doe@example.com", userDTO.getEmail());
        Assertions.assertEquals("IT", userDTO.getDepartment());
        Assertions.assertEquals("Engineer", userDTO.getRole());
        Assertions.assertEquals("Active", userDTO.getStatus());
    }

    @Test
    public void test_fromDTOToEntity() {
        // Given
        when(userDTO.getId()).thenReturn(1L);
        when(userDTO.getUserId()).thenReturn("user123");
        when(userDTO.getFirstName()).thenReturn("John");
        when(userDTO.getLastName()).thenReturn("Doe");
        when(userDTO.getEmail()).thenReturn("john.doe@example.com");
        when(userDTO.getDepartment()).thenReturn("IT");
        when(userDTO.getRole()).thenReturn("Engineer");
        when(userDTO.getStatus()).thenReturn("Active");

        // When
        User user = userServiceImpl.fromDTOToEntity(userDTO);

        // Then
        Assertions.assertEquals((Long) 1L, user.getId());
        Assertions.assertEquals("user123", user.getUserId());
        Assertions.assertEquals("John", user.getFirstName());
        Assertions.assertEquals("Doe", user.getLastName());
        Assertions.assertEquals("john.doe@example.com", user.getEmail());
        Assertions.assertEquals("IT", user.getDepartment());
        Assertions.assertEquals("Engineer", user.getRole());
        Assertions.assertEquals("Active", user.getStatus());
    }

    @Test
    public void testUpdate() {
        // Given
        User userToUpdate = new User();
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        // When
        userService.update(userToUpdate);

        // Then
        verify(userRepository).save(userToUpdate);
    }
}