package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.*;
import com.finalproject.document.management.service.*;
import com.finalproject.document.management.service.interfaces.DepartmentService;
import com.finalproject.document.management.service.interfaces.DocumentCommentService;
import com.finalproject.document.management.service.interfaces.DocumentTransactionService;
import com.finalproject.document.management.service.interfaces.UserTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
@AllArgsConstructor
@RequestMapping("/document-management/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final DocumentCommentService documentCommentService;
    private final DocumentService documentService;
    private final DocumentTransactionService documentTransactionService;
    private final UserTransactionService userTransactionService;
    @Value("${headersUser}")
    private List<String> headers;


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


    @RequestMapping("/getUser/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return userService.findById(id);
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public User findById(Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute(user);
        return user;
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        List<TransactionEntity> transactions = userTransactionService.findAllByUser(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        return "users/view-user";
    }

    @GetMapping("/downloadListAsExcel")
    public ResponseEntity<byte[]> downloadListAsExcel() {
        return userService.downloadListAsExcel();
    }


    @PostMapping("/addNewUser")
    public String addNewUser(
            @RequestParam("userId") String userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("departmentId") Long departmentId,
            @RequestParam("role") String role,
            @RequestParam("password") String password) {
        //generate bcrypt hash
        String pw_hash = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt()) +".q";
        Department department = departmentService.findById(departmentId);

        // Create a new user
        User newUser = new User(userId, firstName, lastName, email, department, role, pw_hash, 1);

        userService.save(newUser);

        return "redirect:/document-management/users/getUsers";
    }


    @GetMapping("/deleteUser") //TO BE FIXED
    public String deleteUser(@RequestParam Long id) {
        // Get user by id
        User user = userService.findById(id);

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
            user.setDepartmentId(departmentId);
        }
        if (password != null) {
//             Generate bcrypt hash
            String passwordHashed = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt()) +".q";
            user.setPassword(passwordHashed);
        }
        if (role != null) {
            user.setRole(role);
        }

        userService.update(user);

        return "redirect:/document-management/users/getUsers";
    }

    //restcontroller endpoints

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

//    @GetMapping("/downloadList")
//    public String downloadList() throws IOException, IllegalAccessException {
//        List<User> documents = userService.findAll();
//        userService.downloadList(documents);
//        return "redirect:/document-management/users/getUsers";
//    }


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
