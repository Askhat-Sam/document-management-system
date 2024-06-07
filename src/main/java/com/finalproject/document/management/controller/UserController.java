package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Department;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.Search;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.DepartmentService;
import com.finalproject.document.management.service.DocumentCommentService;
import com.finalproject.document.management.service.DocumentService;
import com.finalproject.document.management.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
//@RestController
@Controller
@RequestMapping("/document-management/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final DocumentCommentService documentCommentService;
    private final DocumentService documentService;
    @Value("${headersUser}")
    private List<String> headers;

    public UserController(UserService userService, DepartmentService departmentService, DocumentCommentService documentCommentService, DocumentService documentService) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.documentCommentService = documentCommentService;
        this.documentService = documentService;
    }

    @RequestMapping("/getUsers")
    public String showUsers(@RequestParam(name = "page", required = false) Integer page,
                                @RequestParam(name = "size", required = false) Integer size,
                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                @RequestParam(name = "sortDirection", required = false) String sortDirection,
                                @RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(name = "column", required = false) String column,
                                Model model) {
        Search search = new Search();
        List<User> users = userService.findAll(page, size, sortBy, sortDirection, keyword, column);
        User user = new User();
        // Add default department
        Department department = new Department();
        user.setDepartment(department);

        model.addAttribute(user);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("headers", headers);
        return "users/users";
    }
//@RequestMapping("/getUsers")
//public List<User>  showUsers(@RequestParam(name = "page", required = false) Integer page,
//                        @RequestParam(name = "size", required = false) Integer size,
//                        @RequestParam(name = "sortBy", required = false) String sortBy,
//                        @RequestParam(name = "sortDirection", required = false) String sortDirection,
//                        @RequestParam(name = "keyword", required = false) String keyword,
//                        @RequestParam(name = "column", required = false) String column,
//                        Model model) {
//    List<User> users = userService.findAll(page, size, sortBy, sortDirection, keyword, column);
//
//    return users;
//}

    @RequestMapping("/getUser/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%" + user);
        return userService.findById(id);
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public User findById(Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute(user);
        return user;
    }


    @PostMapping("/addNewUser")
    public String addNewUser(
            @RequestParam("userId") String userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("departmentId") Long departmentId,
            @RequestParam("role") String role,
            @RequestParam("password") String password){
        //generate bcrypt hash
//        String pw_hash = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());
        Department department = departmentService.findById(departmentId);

        // Create a new user
        User newUser = new User(userId, firstName, lastName, email, department, role, password, 1);

        userService.save(newUser);

        return "redirect:/document-management/users/getUsers";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        // Get user by id
        User user = userService.findById(id);

        List<Document> documents = documentService.findByUserId(id);

        for (Document document : documents) {
            document.setAuthorId(null);
        }

        System.out.println("To be deleted: " + user);

//        System.out.println("@@@@@@@Comments made by this user: " + id + " followos: " + documentComments);

        userService.deleteUserById(user);
        return "redirect:/document-management/users/getUsers";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(name = "id", required = false) Long id,
                             @RequestParam(name = "userId", required = false) String userId,
                             @RequestParam(name = "firstName", required = false) String firstName,
                             @RequestParam(name = "lastName", required = false) String lastName,
                             @RequestParam(name = "email", required = false) String email,
                             @RequestParam(name = "departmentId", required = false) Long departmentId,
                             @RequestParam(name = "password", required = false) String password,
                             @RequestParam(name = "active", required = false) Integer active,
                             @RequestParam(name = "role", required = false) String role) {

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
            user.setEmail(email);
        }
        if (departmentId != null) {
            Department department = departmentService.findById(departmentId);
            user.setDepartment(department);
        }
        if (password != null) {
            // Generate bcrypt hash
//            String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt()) +".q";
            user.setPassword(password);
        }
        if (role != null) {
            user.setRole(role);
        }

        userService.update(user);

        return "redirect:/document-management/users/getUsers";
    }

    @GetMapping("/downloadList")
    public String downloadList() throws IOException, IllegalAccessException {
        List<User> documents = userService.findAll();
        userService.downloadList(documents);
        return "redirect:/document-management/users/getUsers";
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
