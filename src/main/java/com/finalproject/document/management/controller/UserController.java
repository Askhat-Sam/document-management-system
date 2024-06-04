package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Department;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.DepartmentService;
import com.finalproject.document.management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/document-management/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;

    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @RequestMapping("/getUsers")
    public String showUsers(@RequestParam(name = "page", required = false) Integer page,
                                @RequestParam(name = "size", required = false) Integer size,
                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                @RequestParam(name = "sortDirection", required = false) String sortDirection,
                                @RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(name = "column", required = false) String column,
                                Model model) {
        List<User> users = userService.findAll(page, size, sortBy, sortDirection, keyword, column);
        User user = new User();
        // Add default department
        Department department = new Department();
        user.setDepartment(department);

        model.addAttribute(user);
        model.addAttribute("users", users);
        return "users/users";
    }

    @RequestMapping("/getUser/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

//    @GetMapping("/addNew")
//    public String addNewUser(User user){
//        userService.update(user);
//
//    }


    @PostMapping("/addNewUser")
    public String addNewUser(
            @RequestParam("userId") String userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("departmentId") int departmentId,
            @RequestParam("role") String role,
            @RequestParam("password") String password){
        // Generate bcrypt hash
//        String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());

        Department department = departmentService.findById(departmentId);

        // Create a new user
        User newUser = new User(userId, firstName, lastName, email, department, role, password, 1);

        userService.update(newUser);

        return "redirect:/document-management/users/getUsers";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam int id) {
        // Get user by id
        User user = userService.findById(id);
        userService.deleteUserById(user);
        return "redirect:/document-management/users/getUsers";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(name = "id", required = false) int id,
                             @RequestParam(name = "userId", required = false) String userId,
                             @RequestParam(name = "firstName", required = false) String firstName,
                             @RequestParam(name = "lastName", required = false) String lastName,
                             @RequestParam(name = "email", required = false) String email,
                             @RequestParam(name = "password", required = false) String password,
                             @RequestParam(name = "active", required = false) Integer active,
                             @RequestParam(name = "roleId", required = false) String roleId,
                             @RequestParam(name = "userRole", required = false) String userRole) {

        User user = userService.findById(id);

        if (userId != null) {
            user.setUserId(userId);
        }
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (email != null) {
            user.setUserId(email);
        }
        if (password != null) {
            // Generate bcrypt hash
            //String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(password);
        }
        if (active != null) {
            user.setActive(active);
        }

        userService.update(user);

        return "User with id: " + id + " has been updated";
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
