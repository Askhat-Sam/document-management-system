package com.finalproject.document.management.service;

import com.finalproject.document.management.controller.UserController;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    UserController userController;

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
        Assertions.assertEquals(2, userDTOList.size());
        Assertions.assertEquals("ivan", userDTOList.get(0).getFirstName());
        Assertions.assertEquals("ROLE_MANAGER", userDTOList.get(1).getRole());
        Assertions.assertEquals("Procurement", userDTOList.get(1).getDepartment());
        Assertions.assertEquals("Active", userDTOList.get(0).getStatus());
        Assertions.assertEquals("sergey.s@example.com", userDTOList.get(1).getEmail());
        Assertions.assertEquals("ivanov", userDTOList.get(0).getLastName());
    }
//    @Test
//    public void testFindAllWithParameters() {
//        // given
//        List<UserDTO> userList = new ArrayList<>();
//        userList.add(new UserDTO(1L, "ivan.i", "ivan", "ivanov",
//                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active"));
//        userList.add(new UserDTO(2L, "sergey.s", "sergey", "sergeyev",
//                "sergey.s@example.com", "Procurement", "ROLE_MANAGER", "Not active"));
//        userList.add(new UserDTO(3L, "valeriy.b", "valeriy", "vladov",
//                "valeriy.b@example.com", "Procurement", "ROLE_ADMIN", "Active"));
//        userList.add(new UserDTO(4L, "sergey.v", "sergey", "valdov",
//                "sergey.v@example.com", "Procurement", "ROLE_MANAGER", "Not active"));
//        userList.add(new UserDTO(5L, "anton.s", "anton", "sergeyev",
//                "anton.s@example.com", "Procurement", "ROLE_ADMIN", "Active"));
//        Page<User> pageMock = mock(Page.class);
//        when(pageMock.toList()).thenReturn(userList);
//        UserRepository userRepositoryMock = mock(UserRepository.class);
//        when(userRepositoryMock.findAll(any(Pageable.class))).thenReturn(pageMock);
//
//        // when
//        when(userService.findAll(null, null, "role", null, null, null)).thenReturn(userList);
//        List<UserDTO> userDTOList = userService.findAll(null, null, "role", null, null, null);
//
//        // then
//        Assertions.assertArrayEquals(userList.toArray(), userDTOList.toArray());
//    }


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
        List<String> expectedUserIdsMapped = expectedUserIds.stream().map(User::getUserId).toList();
        when(userRepository.findAllUserIds()).thenReturn(expectedUserIdsMapped);

        // When
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
        User userExpected= new User(1L, "ivan.i", "ivan", "ivanov",
                "ivanov.i@example.com", "IT", "ROLE_EMPLOYEE", "Active");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userExpected));

        // When
        UserDTO userActual = userService.findById(1L);

        // Then
        assertNotNull(userActual); // Ensure user is not null
        assertEquals(userActual.getUserId(), userExpected.getUserId());
        assertEquals("ivan", userActual.getFirstName());
    }
}