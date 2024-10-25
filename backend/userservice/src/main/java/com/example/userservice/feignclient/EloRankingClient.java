package com.example.userservice.feignclient;

import com.example.elorankingservice.dto.Request; // I'm also following the same convention of com.example so this import statement is looking in my own directory
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.GameScore;
import com.example.elorankingservice.entity.PlayerEloRank;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "EloRankingService", url = "http://localhost:8082")
public interface EloRankingClient {

    // Elo rank for users
    @GetMapping("/api/elo-ranking/player/{playerId}/tournament/{tournamentId}")
    PlayerEloRank getPlayerEloRank(@PathVariable long playerId, @PathVariable long tournamentId);

    @GetMapping("/api/elo-ranking/player/tournament")
    Map<String, Object> getSelectedPlayerEloRanks(@RequestBody Request.GetSelectedPlayerEloRanks getSelectedPlayerEloRanks);

    // Elo rank for clans - but clan doesn't have eloRank as attribute so idk if need?
    @GetMapping("/api/elo-ranking/clan/{clanId}/tournament/{tournamentId}")
    ClanEloRank getClanEloRank(@PathVariable Long clanId, @PathVariable Long tournamentId);

    @GetMapping("/api/elo-ranking/clan/tournament/{tournamentId}")
    Map<String, Object> getAllClanEloRanks(@PathVariable Long tournamentId);
}
