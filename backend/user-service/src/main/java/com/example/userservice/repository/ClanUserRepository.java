package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.userservice.entity.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClanUserRepository extends JpaRepository<ClanUser, Long>{
    // Custom method to find ClanUser by Clan ID
    List<ClanUser> findByClan_ClanId(Long clanId);
    // Query to find the first ClanUser entity by user ID
    Optional<ClanUser> findFirstByUserUserId(Long userId);
} 
