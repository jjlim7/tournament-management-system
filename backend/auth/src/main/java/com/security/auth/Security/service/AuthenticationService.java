package com.security.auth.Security.service;


import com.security.auth.Security.model.AuthenticationResponse;
import com.security.auth.Security.model.Token;
import com.security.auth.Security.repository.TokenRepository;
import com.security.auth.User.User;
import com.security.auth.User.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        // check if user already exist. if exist than authenticate the user
        if(userRepository.findByEmail(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist" , null);
        }
        //create new person
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRank("UNRANKED");
        User newUser = userRepository.save(request);

        //generate token and save token for user
        String jwt = jwtService.generateToken(newUser);
        saveUserToken(jwt, newUser);

        return new AuthenticationResponse(jwt, "User registration was successful",newUser);
    }

    public AuthenticationResponse authenticate(Map<String,String> request) {
        //authenticate the user
        String email = request.get("email");
        String password = request.get("password");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( email, password ));

        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new BadAuthenticationException("User not found"));
        String jwt = jwtService.generateToken(user);

        //set all previous token to invalid
        revokeAllTokenByUser(user);

        //save new token, user log in successfully
        saveUserToken(jwt, user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwt);
        response.setMessage("User login was successful");
        response.setUser(user); // Set the User object here

        return response;
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getUsername());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}