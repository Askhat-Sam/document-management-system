package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(Integer page, Integer size, String sortBy, String sortDirection);

    void update(User theUser);

    void deleteUserById(User theUser);

    User findById(Integer theId);

}