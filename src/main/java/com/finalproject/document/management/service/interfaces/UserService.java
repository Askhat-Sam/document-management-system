package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);
    List<UserDTO> findAll();
    void update(User user);
    void save(User user);
    List<String> findAllUserIds();

    void deleteUserById(User user);

    User findById(Long id);
    User getOldUserById(Long id);

    ResponseEntity<byte[]> downloadListAsExcel();


}