package com.finalproject.document.management.aspect;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.TransactionUser;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.DocumentService;
import com.finalproject.document.management.service.interfaces.DocumentTransactionService;
import com.finalproject.document.management.service.UserService;
import com.finalproject.document.management.service.interfaces.UserTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
    private final UserTransactionService userTransactionService;
    private final DocumentTransactionService documentTransactionService;
    private final UserService userService;
    private final DocumentService documentService;

    public TransactionAspect(UserTransactionService userTransactionService, DocumentTransactionService documentTransactionService, UserService userService, DocumentService documentService) {
        this.userTransactionService = userTransactionService;
        this.documentTransactionService = documentTransactionService;
        this.userService = userService;
        this.documentService = documentService;
    }


    @Before("execution(* com.finalproject.document.management.service.UserService.update(..)) ")
    public void beforeUserUpdate(JoinPoint joinPoint) {

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
                        "User Id has been changed from '" + userBeforeUpdate.getUserId() + "' to '" + userAfterUpdate.getUserId() + "'"
                ));
            }
            if (!userAfterUpdate.getFirstName().equals(userBeforeUpdate.getFirstName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User first name has been changed from '" + userBeforeUpdate.getFirstName() + "' to '" + userAfterUpdate.getFirstName() + "'"
                ));
            }
            if (!userAfterUpdate.getLastName().equals(userBeforeUpdate.getLastName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User last name has been changed from '" + userBeforeUpdate.getLastName() + "' to '" + userAfterUpdate.getLastName() + "'"
                ));
            }
            if (!userAfterUpdate.getEmail().equals(userBeforeUpdate.getEmail())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User email has been changed from '" + userBeforeUpdate.getEmail() + "' to '" + userAfterUpdate.getEmail() + "'"
                ));
            }
            if (!userAfterUpdate.getDepartment().getName().equals(userBeforeUpdate.getDepartment().getName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User department has been changed from '" + userBeforeUpdate.getDepartment().getName() + "' to '" + userAfterUpdate.getDepartment().getName() + "'"
                ));
            }
            if (!userAfterUpdate.getRole().equals(userBeforeUpdate.getRole())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User role has been changed from '" + userBeforeUpdate.getRole() + "' to '" + userAfterUpdate.getRole() + "'"
                ));
            }
            if (!userAfterUpdate.getPassword().equals(userBeforeUpdate.getPassword())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User (" + userBeforeUpdate.getUserId() + ") password has been changed"
                ));
            }
            // Check changes of other fields if necessary
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t -> userTransactionService.save(t));
    }

    @Around("execution(* com.finalproject.document.management.service.UserService.save(..)) ")
    public void beforeAddNewUser(ProceedingJoinPoint joinPoint) throws Throwable {

        // Get the user object passed to the save method
        Object userJointPoint = joinPoint.getArgs()[0];
        User user = (User) userJointPoint;

        // Because the user.getId() return null value when creating TransactionUser
        joinPoint.proceed();

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Add new transaction to transactionList
        transactionList.add(new TransactionUser(
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                user.getId(),
                "New user '" + user.getUserId() + "' has been added."
        ));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t -> userTransactionService.save(t));
    }

    @AfterReturning("execution(* com.finalproject.document.management.service.UserService.downloadListAsExcel(..)) ")
    public void afterDownloadingUserList(JoinPoint joinPoint) throws Throwable {

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Add new transaction to transactionList
        transactionList.add(new TransactionUser(
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                null,
                "List of users has been downloaded."
        ));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t -> userTransactionService.save(t));
    }

    @AfterReturning("execution(* com.finalproject.document.management.service.DocumentService.downloadListAsExcel(..)) ")
    public void afterDownloadingDocumentList(JoinPoint joinPoint) throws Throwable {

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Add new transaction to transactionList
        transactionList.add(new TransactionUser(
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                null,
                "List of documents has been downloaded."
        ));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t -> userTransactionService.save(t));
    }

    @Before("execution(* com.finalproject.document.management.service.DocumentServiceImpl.update(..)) ")
    public void beforeDocumentUpdate(JoinPoint joinPoint){

        // Get the user object passed to the update method
        Object documentJointPoint = joinPoint.getArgs()[0];
        Document documentAfterUpdate = (Document) documentJointPoint;

        // Get old version of user from DB
        Document documentBeforeUpdate = documentService.findById(documentAfterUpdate.getId());

        // List for keeping transactions
        List<TransactionUser> transactionList = new ArrayList<>();

        // Check if the user was updated
        if (!documentAfterUpdate.equals(documentBeforeUpdate)) {
            // Check changes of documentCode
            if (!documentAfterUpdate.getDocumentCode().equals(documentBeforeUpdate.getDocumentCode())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document code has been changed from '" + documentBeforeUpdate.getDocumentCode() + "' to '" + documentAfterUpdate.getDocumentCode() + "'"
                ));
            }
            // Check changes of documentTypeId
            if (!documentAfterUpdate.getDocumentTypeId().equals(documentBeforeUpdate.getDocumentTypeId())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document type has been changed from '" + documentBeforeUpdate.getDocumentTypeId() +
                                "' to '" + documentAfterUpdate.getDocumentTypeId() + "'"
                ));
            }
            // Check changes of document name
            if (!documentAfterUpdate.getName().equals(documentBeforeUpdate.getName())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document title has been changed from '" + documentBeforeUpdate.getName() +
                                "' to '" + documentAfterUpdate.getName() + "'"
                ));
            }
            // Check changes of document revision number
            if (!documentAfterUpdate.getRevisionNumber().equals(documentBeforeUpdate.getRevisionNumber())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document revision number has been changed from '" + documentBeforeUpdate.getRevisionNumber() +
                                "' to '" + documentAfterUpdate.getRevisionNumber() + "'"
                ));
            }
            // Check changes of document status id
            if (!documentAfterUpdate.getStatusId().equals(documentBeforeUpdate.getStatusId())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document status id number has been changed from '" + documentBeforeUpdate.getStatusId() +
                                "' to '" + documentAfterUpdate.getStatusId() + "'"
                ));
            }
            // Check changes of document creation date
            if (!documentAfterUpdate.getCreationDate().equals(documentBeforeUpdate.getCreationDate())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document creation date has been changed from '" + documentBeforeUpdate.getCreationDate() +
                                "' to '" + documentAfterUpdate.getCreationDate() + "'"
                ));
            }
            // Check changes of document modification date
            if (!documentAfterUpdate.getModificationDate().equals(documentBeforeUpdate.getModificationDate())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document modification date has been changed from '" + documentBeforeUpdate.getModificationDate() +
                                "' to '" + documentAfterUpdate.getModificationDate() + "'"
                ));
            }
            // Check changes of document author id
            if (!documentAfterUpdate.getAuthorId().equals(documentBeforeUpdate.getAuthorId())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        documentBeforeUpdate.getId(),
                        "Document author id has been changed from '" + documentBeforeUpdate.getAuthorId() +
                                "' to '" + documentAfterUpdate.getAuthorId() + "'"
                ));
            }
            // Check changes of other fields if necessary
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(documentTransactionService::save);
    }


}