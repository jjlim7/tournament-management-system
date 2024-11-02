package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository; // to extend JPA
import org.springframework.stereotype.Repository; // for the @Repository annotation
import com.example.userservice.entity.*;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email); // Find a list of users with the same id
}
