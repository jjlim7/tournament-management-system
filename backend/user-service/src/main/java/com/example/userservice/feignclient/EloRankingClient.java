package com.example.userservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "elo-ranking-service", url = "http://elo-ranking-service:8080")
public interface EloRankingClient {
    @GetMapping("/api/elo-ranking/player/{playerId}/latest-rank")
    ResponseEntity<Map<String, Object>> getPlayerLatestRank(@PathVariable("playerId") Long playerId);

    @GetMapping("/api/elo-ranking/clan/{clanId}/latest-rank")
    ResponseEntity<Map<String, Object>> getClanLatestRank(@PathVariable("clanId") Long clanId);
}