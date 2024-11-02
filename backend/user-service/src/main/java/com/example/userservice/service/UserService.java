package com.example.userservice.service;

// need import statements 
import com.example.userservice.entity.*;
// import com.example.userservice.repository.*; this is not needed
import java.util.*;


public interface UserService {
    // CRUD
    List<User> listAllUsers();
    User addUser(User user);
    User getUser(Long userId);
    User updateUser(Long userId, User newUser); // 2 parameters - id to fetch existing user, user - to make updates eg change email, password etc
    void deleteUser(Long userId);

} 
