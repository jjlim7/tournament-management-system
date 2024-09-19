package com.example.tournamentservice.repository;

import com.example.tournamentservice.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TournamentRepository extends JpaRepository<T, Long> {
    
}