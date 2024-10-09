package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.userservice.entity.*;

@Repository
public interface ClanUserRepository extends JpaRepository<ClanUser, Long>{

} 
