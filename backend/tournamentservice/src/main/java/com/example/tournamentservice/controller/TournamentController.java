package com.example.tournamentservice.controller;
import com.example.tournamentservice.entity.tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/tournaments")

public class TournamentController{

    @Autowired
    private TournamentService tournamentService;
    private final String secretKey = "";

    @GetMapping
    public List<Tournament> getAllTournaments(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String name){
        return tournamentService.getAllTournaments(status, name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id)
            .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));
        return ResponseEntity.ok(tournament);
    }

    @PostMapping
    public Tournament createTournament(@RequestBody Tournament tournament, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        LocalDate today = LocalDate.now();
        String roles = extractRoleFromJWT(token);

        if (!roles.contains("ADMIN")) {
            throw new UnauthorizedException("Only admins can create tournaments");
        }

        if(!tournament.getStartDate().isAfter(today)){
            return ResponseEntity.badRequest().body("Start date must be a future date");
        }

        if(!tournament.getEndDate().isAfter(tournament.getStartDate())){
            return ResponseEntity.badRequest().body("End date cannot be before start date");
        }

        Tournament savedTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournament);
    }

    private String extractRoleFromJWT(String token) {
        String jwt = token.substring(7);
        Claims claims = Jwts.parser()
        .setSigningKey(secretKey.getBytes())
        .parseClaimsJws(jwt)
        .getBody();

        return (String) claims.get("roles");
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