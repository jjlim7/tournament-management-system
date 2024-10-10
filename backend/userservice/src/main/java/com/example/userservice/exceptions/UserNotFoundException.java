package com.example.userservice.exceptions;

import org.springframework.http.HttpStatus; // This is to make the exception work together with the HTTP status code.
import org.springframework.web.bind.annotation.ResponseStatus; // This allows you to return specific HTTP status codes for exceptions.

@ResponseStatus(HttpStatus.NOT_FOUND) // Returns 404 Error
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(Long userId) {
        super("Could not find user with UserId: " + userId);
    }
}
