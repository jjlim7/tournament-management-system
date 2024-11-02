package com.security.auth.Security.service;

public class BadAuthenticationException extends RuntimeException{
    public BadAuthenticationException(String msg){
        super("BAD REQUEST: " + msg);
    }
}



