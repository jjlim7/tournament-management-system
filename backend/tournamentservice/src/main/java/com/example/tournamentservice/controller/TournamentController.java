package com.example.tournamentservice.controller;

import com.example.tournamentservice.service.TournamentService;
import com.example.tournamentservice.entity.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")

public class TournamentController{

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament){
        Tournament createdTournament = tournamentService.createTournament(tournament);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    
    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getAllTournament(){
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

   // @GetMapping("/{id}")
   // public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
   //     Tournament tournament = tournamentService.getTournamentById(id)
   //         .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));
   //     return ResponseEntity.ok(tournament);
   // }

    //@PostMapping
    //public ResponseEntity<String> createTournament(@RequestBody Tournament tournament, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        //Date today = LocalDate.now();
      //  String roles = extractRoleFromJWT(token);

        //if (!roles.contains("ADMIN")) {
          //  throw new Exception("Only admins can create tournaments");
       // }

        //if(!tournament.getStartDate().after("20/9/2023")){
         //   return ResponseEntity.badRequest().body("Start date must be a future date");
       // }

        //if(!tournament.getEndDate().after(tournament.getStartDate())){
        //    return ResponseEntity.badRequest().body("End date cannot be before start date");
        //}

       // Tournament savedTournament = tournamentService.createTournament(tournament);
        //return ResponseEntity.status(HttpStatus.CREATED).body(savedTournament);
    //}


    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        Tournament updatedTournament = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(updatedTournament);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}