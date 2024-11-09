package com.security.auth.Security.Controller;

import com.security.auth.Security.config.CustomLogoutHandler;
import com.security.auth.Security.model.AuthenticationResponse;
import com.security.auth.Security.service.AuthenticationService;
import com.security.auth.Security.service.BadAuthenticationException;
import com.security.auth.Security.service.JwtService;
import com.security.auth.User.User;
import com.security.auth.User.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Controller {
    private final AuthenticationService authService;
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public Controller(AuthenticationService authService,
                      CustomLogoutHandler customLogoutHandler,
                      JwtService jwtService,
                      UserRepository userRepository){
        this.authService = authService;
        this.customLogoutHandler = customLogoutHandler;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody User request ) {
        return ResponseEntity.ok(authService.register(request));
    }

    //will return the token,message and person object
    @PostMapping("/api/login")
    public ResponseEntity<AuthenticationResponse> login( @RequestBody Map<String,String> request) {
        System.out.println(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        customLogoutHandler.logout(request, response, null);
        return ResponseEntity.ok("Logged out successfully");
    }

    // this is for testing
    @PostMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello, you are authenticated and authorized!");
    }
}
