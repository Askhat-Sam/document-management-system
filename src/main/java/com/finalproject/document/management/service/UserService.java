package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    void save(User theUser);\

    void update(User theUser);

    void deleteUserById(User theUser);

    User findById(Integer theId);

    long getUserCount();

}