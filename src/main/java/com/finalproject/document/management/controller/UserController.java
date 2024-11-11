package com.finalproject.document.management.controller;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.Search;
import com.finalproject.document.management.entity.TransactionEntity;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model, @RequestParam(name = "errorMessage", required = false) String errorMessage,
                           @RequestParam(name = "userIdExist", required = false) String userIdExist) {
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
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("userIdExist", userIdExist);


        return VIEW_USER;
    }

    @PostMapping("/updateUser")
    public String updateUser(RedirectAttributes redirectAttributes,
                             Model model,
                             @Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            model.addAttribute("departments", user.getDepartment());
            model.addAttribute("statuses", user.getStatus());
            model.addAttribute("roles", user.getRole());
            return VIEW_USER;
        } else {
            User updatedUser = userService.updateUser(user,redirectAttributes);
            userService.update(updatedUser);// Return page with the error message
            if (redirectAttributes.getAttribute("errorMessage") != null) {
                return "redirect:/document-management/users/view/" + user.getId();
            }

            if (redirectAttributes.getAttribute("userIdExist") != null) {
                return "redirect:/document-management/users/view/" + user.getId();
            }
        }
        return REDIRECT_USERS;
    }

}
