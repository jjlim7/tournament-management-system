package com.security.auth.Security.model;

import com.security.auth.User.User;


public class AuthenticationResponse {
    private String token;
    private String message;
    private User user;

    public AuthenticationResponse(String token, String message, User user) {
        this.token = token;
        this.message = message;
        this.user = user;
    }
    public AuthenticationResponse(){}
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "token='" + token + '\'' +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
