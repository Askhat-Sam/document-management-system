package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);
    List<User> findAll();
    void update(User user);
    void save(User user);

    void deleteUserById(User user);

    User findById(Long id);
    User getOldUserById(Long id);

    ResponseEntity<byte[]> downloadListAsExcel();

    User findUserByIdWithDocuments(Long id);

}