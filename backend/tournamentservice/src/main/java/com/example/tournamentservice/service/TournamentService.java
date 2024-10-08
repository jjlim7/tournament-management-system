package com.example.tournamentservice.service;

import com.example.tournamentservice.*;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    //Creation of tournament with some validation of dates.
    //Assumption is that start and end date cannot be the same
    public Tournament createTournament(Tournament tournament) {

        if(tournament.getStartDate().isBefore(OffsetDateTime.now())){
            throw new IllegalArgumentException("Start date must be in the future");
        }

        if (tournament.getEndDate().isBefore(tournament.getStartDate()) || tournament.getEndDate().isEqual(tournament.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be same or before the starting date");
        }

        return tournamentRepository.save(tournament);
    }

    //Retrieval of all the tournaments
    public List<Tournament> getAllTournaments(){
        return tournamentRepository.findAll();
    }

    //Retrieval of tournament by ID
    public Optional<Tournament> getTournamentById(Long id){
        return tournamentRepository.findById(id);
    }

    //Updating of tournament information
    public Tournament updateTournament(Long id, Tournament tournament) {
        Optional<Tournament> optionalTournament = tournamentRepository.findById(id);
        if (!optionalTournament.isPresent()) {
            throw new RuntimeException("Tournament not found");
        }

        Tournament existingTournament = optionalTournament.get();
        
        // Ensure that admin does not update player capacity lower than the current player capacity inside tournament
        if (tournament.getPlayerCapacity() != 0 && tournament.getPlayerCapacity() > existingTournament.getPlayerCapacity()) {
            existingTournament.setPlayerCapacity(tournament.getPlayerCapacity());
        }

        // Assumption that while editing start date, it cannot be before present time.
        if (tournament.getStartDate() != null) {
            if (tournament.getStartDate().isBefore(OffsetDateTime.now())) {
                throw new IllegalArgumentException("Start date cannot be in the past");
            }
            existingTournament.setStartDate(tournament.getStartDate());
        }

        if (tournament.getEndDate() != null) {
            if (tournament.getEndDate().isBefore(existingTournament.getStartDate()) || tournament.getEndDate().isEqual(existingTournament.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be the same as or before the start date");
            }
            existingTournament.setEndDate(tournament.getEndDate());
        }

        // Update the status based on the start date
        updateTournamentStatus(existingTournament);

        return tournamentRepository.save(existingTournament);
    } 

    public void deleteTournament(Long id) {
        // Check if the tournament exists
        if (!tournamentRepository.existsById(id)) {
            throw new IllegalArgumentException("Tournament not found with ID: " + id);
        }
        
        // If it exists, delete the tournament
        tournamentRepository.deleteById(id);
    }

    private void updateTournamentStatus(Tournament tournament) {
        OffsetDateTime now = OffsetDateTime.now();
        if (tournament.getStartDate().isBefore(now)) {
            tournament.setStatus(Tournament.Status.Active);
        } else {
            tournament.setStatus(Tournament.Status.Rescheduled);
        }
    }

    //Need to do test case for this and controller layer for joinTournament
    public String joinTournament(Long tournamentId, Long playerId) {
        // Retrieve the tournament by ID
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Check if the player is already in the tournament
        if (tournament.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Player is already registered in this tournament.");
        }

        // Check if the tournament has reached its player capacity
        if (tournament.getPlayerIds().size() >= tournament.getPlayerCapacity()) {
            throw new IllegalArgumentException("Tournament has reached its player capacity.");
        }

        // Add player to the tournament
        tournament.getPlayerIds().add(playerId);
        tournamentRepository.save(tournament);

        return "Player joined the tournament successfully.";

    }

    //Need to do a logic for leavingTournament
    //Need to do test case for this too
    //Need to do controller layer for leaveTournament too
}

