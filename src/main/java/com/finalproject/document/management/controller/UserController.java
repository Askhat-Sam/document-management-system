package com.finalproject.document.management.controller;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.Search;
import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/document-management/users")
public class UserController {
    private final UserService userService;
    private final DocumentCommentService documentCommentService;
    private final DocumentService documentService;
    private final DocumentTransactionService documentTransactionService;
    private final UserTransactionService userTransactionService;
    private final DocumentValidationService documentValidationService;
    private static final String USERS = "users/users";
    private static final String VIEW_USER = "users/view-user";
    private static final String REDIRECT_USERS = "redirect:/document-management/users/getUsers";
    @Value("${headersUser}")
    private List<String> headers;
    @Value("${departments}")
    private List<String> departments;
    @Value("${roles}")
    private List<String> roles;
    @Value("${statuses}")
    private List<String> statuses;

    @RequestMapping("/getUsers")
    public String getUsers(@RequestParam(name = "page", required = false) Integer page,
                           HttpServletRequest request, // another option
                           @RequestParam(name = "size", required = false) Integer size,
                           @RequestParam(name = "sortBy", required = false) String sortBy,
                           @RequestParam(name = "sortDirection", required = false) String sortDirection,
                           @RequestParam(name = "keyword", required = false) String keyword,
                           @RequestParam(name = "column", required = false) String column,
                           Model model) {
        Search search = new Search();
        List<UserDTO> users = userService.findAll(page, size, sortBy, sortDirection, keyword, column);
        User user = new User();
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute(user);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("headers", headers);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        model.addAttribute("loggedUser", loggedUser);

        return USERS;
    }

    @RequestMapping("/getUser/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        UserDTO user = userService.findById(id);
        List<TransactionEntity> transactionsUsers = userTransactionService.findAllByUser(user.getUserId());
        List<TransactionEntity> transactionsDocuments = documentTransactionService.findAllByUser(user.getUserId());
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute("user", user);
        model.addAttribute("transactionsUsers", transactionsUsers);
        model.addAttribute("transactionsDocuments", transactionsDocuments);
        model.addAttribute("statuses", statuses);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);

        return VIEW_USER;
    }

    @GetMapping("/downloadListAsExcel")
    public ResponseEntity<byte[]> downloadListAsExcel() {
        return userService.downloadListAsExcel();
    }


    @PostMapping("/addNewUser")
    public String addNewUser(
//            @RequestParam("userId") String userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("department") String department,
            @RequestParam("role") String role,
            @RequestParam("status") String status,
            @RequestParam("password") String password) {
        //generate bcrypt hash
        String pw_hash = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());

        //Generate userId using firstName and lastName
        String userId = userService.generateUserId(firstName, lastName);

        // Create a new user
        User newUser = new User(userId, firstName, lastName, email, department, role, status, pw_hash, 1);

        userService.save(newUser);

        return REDIRECT_USERS;
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(name = "id", required = false) Long id,
                             @RequestParam(name = "userId", required = false) String userId,
                             @RequestParam(name = "firstName", required = false) String firstName,
                             @RequestParam(name = "lastName", required = false) String lastName,
                             @RequestParam(name = "email", required = false) String email,
                             @RequestParam(name = "department", required = false) String department,
                             @RequestParam(name = "password", required = false) String password,
                             @RequestParam(name = "active", required = false) Integer active,
                             @RequestParam(name = "role", required = false) String role,
                             @RequestParam(name = "status", required = false) String status) {

        User user = userService.updateUser(id, userId, firstName, lastName, email, department, password, role, status);

        userService.update(user);

        return REDIRECT_USERS;
    }

}
