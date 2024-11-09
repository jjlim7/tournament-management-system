package com.example.tournamentservice.repository;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.tournamentservice.entity.Tournament;

@Service
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    Optional<Tournament> findByName(String name);

    List<Tournament> findByStartDateBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    List<Tournament> findByEndDateBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query("SELECT t FROM Tournament t WHERE t.startDate BETWEEN :startDate AND :endDate OR t.endDate BETWEEN :startDate AND :endDate")
    List<Tournament> findByDateRange(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'INACTIVE'")
    List<Tournament> findUpcomingTournaments();
}