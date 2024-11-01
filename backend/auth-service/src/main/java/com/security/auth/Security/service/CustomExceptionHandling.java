//package com.security.auth.Security.service;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//
//@ControllerAdvice
//public class CustomExceptionHandling extends ResponseEntityExceptionHandler {
//
//
////    @ExceptionHandler(value = CustomerBadRequestException.class)
////    public ResponseEntity<Object> handleCustomerBadRequestException (CustomerBadRequestException e) {
////        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
////
////        Exception message = new Exception(
////                badRequest.value(),
////                e.getMessage()
////        );
////
////        // RESPONSE ENTITY
////        return new ResponseEntity<>(message, badRequest);
////    }
//
//}
//
