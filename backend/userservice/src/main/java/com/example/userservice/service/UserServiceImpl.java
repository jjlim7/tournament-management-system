package com.example.userservice.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // to be able to use the @Service annotation

import com.example.userservice.entity.*;
import com.example.userservice.repository.*;

import jakarta.transaction.Transactional;

import java.util.*;


@Service 
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userDB;

    public UserServiceImpl(UserRepository userDB) {
        this.userDB = userDB;
    }

    @Override
    @Transactional
    public List<User> listAllUsers() {
        return userDB.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) {

        List<User> sameUsersList = userDB.findByEmail(user.getEmail());

        if(sameUsersList.size() == 0) { // size = 0 means unique user, add him to the DB
            return userDB.save(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public User getUser(Long userId) {
        /* findById() is of type optional */
        return userDB.findById(userId).orElse(null);

    }

    @Override
    @Transactional
    public User updateUser(Long userId, User newUser) {
        Optional<User> optionalUser = userDB.findById(userId);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            user.setRole(newUser.getRole());
            user.setCountry(newUser.getCountry());
            user.setElo_rating(newUser.getElo_rating());
            user.setRank(newUser.getRank());
            
            return userDB.save(user); // need to save the updates in the DB

        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userDB.findById(userId);

        if(optionalUser.isPresent()) {
            userDB.deleteById(userId);
        }

    }
 }
