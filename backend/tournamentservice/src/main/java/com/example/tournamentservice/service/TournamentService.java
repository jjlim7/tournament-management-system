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

    public List<Tournament> getTournaments(String status, String name){
        //Filter tournament by status and name
        if (status != null) {
            if(name != null){
                return tournamentRepository.findByStatusAndName(status, name);
            }
            return tournamentRepository.findByStatus(status);
        }
        if (name != null){
            return tournamentRepository.findByStatusAndName(status, name);
        }
        return tournamentRepository.findAll();
    }

    // More things to consider?


    //Tournament list that is distinct in terms of status
    //Tournament status will have to be dependent on start and end date
    //Shouldnt be able to edit game mode also.
    //Check that it is the admin that is in charge of the tournamnet before giving permission to edit

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
        tournament.setStartDate(tournamentDetails.getStartDate());
        tournament.setEndDate(tournamentDetails.getEndDate()); 
        tournament.setPlayerCapacity(tournamentDetails.getPlayerCapacity()); //need to check current player count before user can update
        tournament.setStatus(tournamentDetails.getStatus()); //might not need 
        tournament.setGameMode(tournamentDetails.getGameMode()); //dont make sense to update 
        //This might need to change
        return tournamentRepository.save(tournament);
    }
}

