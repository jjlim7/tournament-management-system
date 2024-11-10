package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.userservice.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClanUserRepository extends JpaRepository<ClanUser, Long>{
    // Custom method to find ClanUser by Clan ID
    List<ClanUser> findByClan_ClanId(Long clanId);
    @Query("SELECT cu FROM ClanUser cu JOIN FETCH cu.clan WHERE cu.user.userId = :userId")
    Optional<ClanUser> findFirstByUserUserIdWithClan(@Param("userId") Long userId);
} 
