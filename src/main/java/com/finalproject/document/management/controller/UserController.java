package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Role;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-management")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public List<User> showUsers(){
        return userService.findAll();
    }

    @RequestMapping("/getUser/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/addNewUser")
    public String addNewUser(
                             @RequestParam("userId") String userId,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("active") int active,
                             @RequestParam("roleId") String roleId,
                             @RequestParam("userRole") String userRole){
        // Generate bcrypt hash
//        String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a new user
        User newUser = new User(userId, firstName, lastName, email, password, 1);

        // Add role to use
        newUser.add(new Role(roleId,userRole));

        userService.update(newUser);

        return "User with user id: " + userId + " has been added";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam int id){
        // Get user by id
        User user = userService.findById(id);
        userService.deleteUserById(user);
        return "User with id: " + id + " has been deleted";
    }

//    @PostMapping("/addNewUser")
//    public String addNewUser(@RequestBody User user){
////        // Generate bcrypt hash
////        String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());
////
////        // Create a new user
////        User newUser = new User(userId, firstName, lastName, email, passwordHashed, 1);
////
////        // Add role to use
////        newUser.add(new Role(roleId,userRole));
//
//        userService.update(user);
//
//        return "redirect:/document-management/users";
//    }
}
