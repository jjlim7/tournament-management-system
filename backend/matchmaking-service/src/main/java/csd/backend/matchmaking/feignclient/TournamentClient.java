package csd.backend.matchmaking.feignclient;

import csd.backend.matchmaking.feigndto.Tournament;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "TournamentService", url = "${tournament.service.url}")
public interface TournamentClient {

    @GetMapping("/api/tournaments/{id}")
    ResponseEntity<Tournament> getTournamentById(@PathVariable Long id);

    @GetMapping("/api/tournaments/upcoming")
    List<Tournament> getUpcomingTournaments();

    @PutMapping("/api/tournaments/{tournamentId}/status")
    void updateTournamentStatus(@PathVariable("tournamentId") Long tournamentId, @RequestParam("newStatus") String newStatus);
}
