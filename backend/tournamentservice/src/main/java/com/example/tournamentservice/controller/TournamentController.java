package com.example.tournamentservice.controller;
import com.example.tournamentservice.entity.tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/tournaments")

public class TournamentController{

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id)
            .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));
        return ResponseEntity.ok(tournament);
    }

    @PostMapping
    public Tournament createTournament(@RequestBody Tournament tournament){
        LocalDate today = LocalDate.now();

        if(!tournament.getStartDate().isAfter(today)){
            return ResponseEntity.badRequest().body("Start date must be a future date");
        }

        if(!tournament.getEndDate().isAfter(tournament.getStartDate())){
            return ResponseEntity.badRequest().body("End date cannot be before start date");
        }

        Tournament savedTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournament);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @RequestBody Tournament tournamentDetails) {
        Tournament updatedTournament = tournamentService.updateTournament(id, tournamentDetails);
        return ResponseEntity.ok(updatedTournament);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}