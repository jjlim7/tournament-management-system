package com.example.tournamentservice.service;

import com.example.tournamentservice.*;
import com.example.tournamentservice.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;


    public List<Tournament> getAllTournmaments(){
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(long id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));

        tournamentRepository.delete(tournament);
    }

    public Tournament updateTournament(Long id, Tournament tournamentDetails) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));

        tournament.setName(tournamentDetails.getName());
        //tournament.setStartDate(tournamentDetails.getStartDate());
        tournament.setEndDate(tournamentDetails.getEndDate());
        tournament.setPlayerCapacity(tournamentDetails.getPlayerCapacity());
        tournament.setStatus(tournamentDetails.getStatus());
        tournament.setGameMode(tournamentDetails.getGameMode());

        return tournamentRepository.save(tournament);
    }
}

