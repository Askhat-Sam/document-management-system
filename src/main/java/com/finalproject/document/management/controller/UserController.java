package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public User findById(Integer theId) {
        System.out.println("HERE: " + userService.findById(theId));
        return userService.findById(theId);
    }

}
