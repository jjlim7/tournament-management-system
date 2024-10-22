package com.example.tournamentservice.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public class UserFeignClient {
    
    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping

}
