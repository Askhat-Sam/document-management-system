package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll(Integer page, Integer size, String sortBy, String sortDirection, String keyword, String column);

    List<UserDTO> findAll();

    void update(User user);

    void save(User user);

    List<String> findAllUserIds();


    User findUserById(Long id);

    UserDTO findById(Long id);

    ResponseEntity<byte[]> downloadListAsExcel();

    UserDTO fromEntityToDTO(User user);

    User fromDTOToEntity(UserDTO userDTO);

    User updateUser(User user, RedirectAttributes redirectAttributes);
    String generateUserId(String firstName, String lastName);
}