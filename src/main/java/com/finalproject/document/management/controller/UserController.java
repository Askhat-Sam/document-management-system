package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document-management")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public List<User> showAdminUser(){
        return userService.findAll();
    }


}
