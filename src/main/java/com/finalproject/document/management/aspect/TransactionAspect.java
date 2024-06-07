package com.finalproject.document.management.aspect;

import com.finalproject.document.management.entity.TransactionUser;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.DocumentService;
import com.finalproject.document.management.service.UserService;
import com.finalproject.document.management.service.UserTransactionService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class TransactionAspect {

    private UserTransactionService userTransactionService;
    private UserService userService;
    private DocumentService documentService;
    @Autowired
    private EntityManager entityManager;

    public TransactionAspect(UserTransactionService userTransactionService, UserService userService, DocumentService documentService) {
        this.userTransactionService = userTransactionService;
        this.userService = userService;
        this.documentService = documentService;
    }


    @Before("execution(* com.finalproject.document.management.service.UserService.update(..)) ")
    public void beforeUserUpdate(JoinPoint joinPoint){

        // Get the user object passed to the update method
        Object userJointPoint = joinPoint.getArgs()[0];
        User userAfterUpdate = (User) userJointPoint;

        // Get old version of user from DB
        User userBeforeUpdate = userService.getOldUserById(userAfterUpdate.getId());

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Check if the user was updated
        if (!userAfterUpdate.equals(userBeforeUpdate)) {
            // Check changes of userId
            if (!userAfterUpdate.getUserId().equals(userBeforeUpdate.getUserId())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User Id was changed from '" + userBeforeUpdate.getUserId() + "' to '" + userAfterUpdate.getUserId() + "'"
                ));
            }
            if (!userAfterUpdate.getFirstName().equals(userBeforeUpdate.getFirstName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User first name was changed from '" + userBeforeUpdate.getFirstName() + "' to '" + userAfterUpdate.getFirstName() + "'"
                ));
            }
            if (!userAfterUpdate.getLastName().equals(userBeforeUpdate.getLastName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User last name was changed from '" + userBeforeUpdate.getLastName() + "' to '" + userAfterUpdate.getLastName() + "'"
                ));
            }
            if (!userAfterUpdate.getEmail().equals(userBeforeUpdate.getEmail())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User email was changed from '" + userBeforeUpdate.getEmail() + "' to '" + userAfterUpdate.getEmail() + "'"
                ));
            }
            if (!userAfterUpdate.getDepartment().getName().equals(userBeforeUpdate.getDepartment().getName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User department was changed from '" + userBeforeUpdate.getDepartment().getName() + "' to '" + userAfterUpdate.getDepartment().getName() + "'"
                ));
            }
            if (!userAfterUpdate.getRole().equals(userBeforeUpdate.getRole())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User role was changed from '" + userBeforeUpdate.getRole() + "' to '" + userAfterUpdate.getRole() + "'"
                ));
            }
            if (!userAfterUpdate.getPassword().equals(userBeforeUpdate.getPassword())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User password was changed"
                ));
            }
            // Check changes of other fields if necessary
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t->userTransactionService.save(t));
    }

    @Around("execution(* com.finalproject.document.management.service.UserService.save(..)) ")
    public void beforeAddNewUser(ProceedingJoinPoint joinPoint) throws Throwable {

        // Get the user object passed to the update method
        Object userJointPoint = joinPoint.getArgs()[0];
        User user = (User) userJointPoint;

        joinPoint.proceed();

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Add new transaction to transactionList
        transactionList.add(new TransactionUser(
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                user.getId(),
                "New user with id '" + user.getId() + "' has been added."
        ));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t->userTransactionService.save(t));
    }

//    @Before("execution(* com.finalproject.document.management.service.DocumentServiceImpl.update(..)) ")
//    public void beforeUserAdd(JoinPoint joinPoint){
//
//        // Get the user object passed to the update method
//        Object documentJointPoint = joinPoint.getArgs()[0];
//        Document documentAfterUpdate = (Document) documentJointPoint;
//
//        // Get old version of user from DB
//        Document documentBeforeUpdate = documentService.findById(documentAfterUpdate.getId());
//
//        // List for keeping transactions
//        List<TransactionUser> transactionList = new ArrayList<>();
//
//        // Check if the user was updated
//        if (!documentAfterUpdate.equals(documentBeforeUpdate)) {
//            // Check changes of userId
//            if (!documentAfterUpdate.getDocumentCode().equals(documentBeforeUpdate.getDocumentCode())) {
//                transactionList.add(new TransactionUser(
//                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
//                        SecurityContextHolder.getContext().getAuthentication().getName(),
//                        documentBeforeUpdate.getId(),
//                        "Document code was changed from '" + documentBeforeUpdate.getDocumentCode() + "' to '" + documentAfterUpdate.getDocumentCode() + "'"
//                ));
//            }
//
//            // Check changes of other fields if necessary
//        }
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        request.setAttribute("transactionList", transactionList);
//        // Save each userTransaction in transactionList into DB
//        transactionList.forEach(t->userTransactionService.save(t));
//    }
}