package csd.backend.matchmaking.feignclient;

import com.example.elorankingservice.dto.Request;
import com.example.tournamentservice.entity.Tournament;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "TournamentService", url = "http://localhost:8083")
public interface TournamentClient {

    @GetMapping("/tournaments/{id}")
    ResponseEntity<Tournament> getTournamentById(@PathVariable Long id);
}
