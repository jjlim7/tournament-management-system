package csd.backend.matchmaking.feignclient;

import csd.backend.matchmaking.dto.Tournament;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "TournamentService", url = "http://localhost:8083")
public interface TournamentClient {

    @GetMapping("/tournaments/{id}")
    ResponseEntity<Tournament> getTournamentById(@PathVariable Long id);

    @GetMapping("/tournaments/upcoming")
    List<Tournament> getUpcomingTournaments();

    @PutMapping("/tournaments/{tournamentId}/status")
    void updateTournamentStatus(@PathVariable("tournamentId") Long tournamentId, @RequestParam("newStatus") String newStatus);
}
