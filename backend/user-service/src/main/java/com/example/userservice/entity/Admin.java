package com.example.userservice.entity;

public class Admin extends User {
    
    public Admin(Long userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }
}
