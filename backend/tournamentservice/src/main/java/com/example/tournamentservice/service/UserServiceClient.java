package com.example.tournamentservice.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.tournamentservice.DTO.UserDTO;

@FeignClient(name = "user-service", url = "http://user-service:8080") // Update URL accordingly
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserDTO getUserById(@PathVariable Long userId);
}
