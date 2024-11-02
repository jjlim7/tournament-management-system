package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository; // to extend JPA
import org.springframework.stereotype.Repository; // for the @Repository annotation
import com.example.userservice.entity.*;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Long> {
    
}
