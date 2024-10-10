package com.example.userservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.*;
import com.example.userservice.service.*;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Read
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.listAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);

        if(user == null) {
            throw new UserNotFoundException(userId);
        } 

        return user;
    }

    // Create
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 - success
    @PostMapping("/users") // post mapping to add the new user to the entire list of users
    @Transactional
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // Update
    @PutMapping("/users/{userId}") // PUT is to update
    public User updateUserDetails(@PathVariable Long userId, @RequestBody User newUser) {
        User updatedUser = userService.updateUser(userId, newUser);
        
        if(updatedUser == null) {
            throw new UserNotFoundException(userId);
        }

        return updatedUser;
    }

    // Delete
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        
        try {
            userService.deleteUser(userId);

        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(userId);
        }
    }
     
}
