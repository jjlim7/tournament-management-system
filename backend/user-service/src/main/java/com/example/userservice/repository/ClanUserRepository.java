package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.userservice.entity.*;

import java.util.List;

@Repository
public interface ClanUserRepository extends JpaRepository<ClanUser, Long>{
    // Custom method to find ClanUser by Clan ID
    List<ClanUser> findByClan_ClanId(Long clanId);
} 
