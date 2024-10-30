package com.example.tournamentservice.repository;

import com.example.tournamentservice.entity.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    @Query("SELECT t FROM Tournament t WHERE t.status = 'INACTIVE'")
    List<Tournament> findUpcomingTournaments();
}