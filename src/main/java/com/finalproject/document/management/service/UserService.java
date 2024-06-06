package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);
    List<User> findAll();
    void update(User theUser);

    void deleteUserById(User theUser);

    User findById(Long theId);
    User getOldUserById(Long theId);

    <T> String downloadList(List<T> list);

    User findUserByIdWithDocuments(Long id);

}