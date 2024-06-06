package com.finalproject.document.management.aspect;
import com.finalproject.document.management.entity.TransactionUser;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.UserService;
import com.finalproject.document.management.service.UserTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.FieldSignature;
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
    @Autowired
    private UserTransactionService userTransactionService;

    private UserService userService;

    public TransactionAspect(UserService userService) {
        this.userService = userService;
    }

    @Before("execution(* com.finalproject.document.management.service.UserServiceImpl.update(..)) ")
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
            if (!userAfterUpdate.getDepartmentId().equals(userBeforeUpdate.getDepartmentId())) {
                transactionList.add(new TransactionUser(
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        userBeforeUpdate.getId(),
                        "User department id was changed from '" + userBeforeUpdate.getDepartmentId() + "' to '" + userAfterUpdate.getDepartmentId() + "'"
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
            // Check changes of other fields if necessary
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("transactionList", transactionList);
        // Save each userTransaction in transactionList into DB
        transactionList.forEach(t->userTransactionService.save(t));
    }
}