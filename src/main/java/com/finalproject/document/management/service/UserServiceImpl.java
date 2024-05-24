package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User theUser) {
        userRepository.save(theUser);
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

        if (result.isPresent()){
            theUser = result.get();
            System.out.println("result.get(): " + theUser);
        } else {
            throw new RuntimeException("Did not find user id: "+ theId);
        }
        return theUser;

    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

}