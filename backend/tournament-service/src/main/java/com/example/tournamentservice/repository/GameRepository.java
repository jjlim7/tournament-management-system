package com.example.tournamentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.tournamentservice.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

}