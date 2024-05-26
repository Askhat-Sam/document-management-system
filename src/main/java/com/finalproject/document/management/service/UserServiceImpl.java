package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll(Integer page, Integer size, String sortBy, String sortDirection) {
        Pageable pageable = doPagingAndSorting(page, size, sortBy, sortDirection);
        List<User> users;
        if (pageable != null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAll();
        }
        return users;
    }


    @Override
    public void update(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    public void deleteUserById(User theUser) {
        userRepository.delete(theUser);
    }

    @Override
    public User findById(Integer theId) {

        Optional<User> result = userRepository.findById(theId);

        System.out.println("result: " + result);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
            System.out.println("result.get(): " + theUser);
        } else {
            throw new RuntimeException("Did not find user id: " + theId);
        }
        return theUser;

    }

    private static Pageable doPagingAndSorting(Integer page, Integer size, String sortBy, String sortDirection) {
        if (sortBy != null) {
            Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, sortBy);
            // Defining the sort direction: A - ascending and D - descending. If direction not provided, default direction to be applied
            if (sortDirection!=null){
                if (sortDirection.equals("A")) {
                    sort = Sort.by(ASC, sortBy);
                } else if (sortDirection.equals("D")) {
                    sort = Sort.by(DESC, sortBy);
                }
            }

            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size, sort);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            }
            return pageable;
        } else {
            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE);
            }
            return pageable;
        }
    }

}