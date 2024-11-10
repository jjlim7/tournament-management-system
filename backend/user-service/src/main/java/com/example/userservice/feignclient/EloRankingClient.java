package com.example.userservice.feignclient;

import com.example.userservice.feigndto.GameMode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "elo-ranking-service", url = "http://elo-ranking-service:8080")
public interface EloRankingClient {

    @GetMapping("/api/elo-ranking/player/{playerId}/latest-rank")
    ResponseEntity<Map<String, Object>> getPlayerLatestRank(@PathVariable("playerId") Long playerId);

    @GetMapping("/api/elo-ranking/clan/{clanId}/latest-rank")
    ResponseEntity<Map<String, Object>> getClanLatestRank(@PathVariable("clanId") Long clanId);

    @GetMapping("/api/game-score/player/{playerId}/tournament/{tournamentId}/stats")
    ResponseEntity<Map<String, Object>> getPlayerStatistics(
            @PathVariable("playerId") Long playerId,
            @PathVariable("tournamentId") Long tournamentId,
            @RequestParam(value = "gameMode", required = false) GameMode gameMode
    );

    @GetMapping("/api/game-score/clan/{clanId}/tournament/{tournamentId}/stats")
    ResponseEntity<Map<String, Object>> getClanStatistics(
            @PathVariable("clanId") Long clanId,
            @PathVariable("tournamentId") Long tournamentId
    );
}
