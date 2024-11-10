package csd.backend.matchmaking.feignclient;

import csd.backend.matchmaking.feigndto.ClanEloRank;
import csd.backend.matchmaking.feigndto.PlayerEloRank;
import csd.backend.matchmaking.dto.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "EloRankingService", url = "${elo.service.url}")
public interface EloRankingClient {

    @GetMapping("/api/elo-ranking/player/{playerId}/tournament/{tournamentId}")
    Map<String, Object> getPlayerEloRank(@PathVariable long playerId, @PathVariable long tournamentId);

    @GetMapping("/api/elo-ranking/player/tournament")
    Map<String, Object> getSelectedPlayerEloRanks(@RequestBody Request.GetSelectedPlayerEloRanks getSelectedPlayerEloRanks);

    @GetMapping("/api/game-score/player/{playerId}/tournament/{tournamentId}")
    Map<String, Object> getPlayerGameScores(@PathVariable Long playerId, @PathVariable Long tournamentId);

    @GetMapping("/api/elo-ranking/clan/{clanId}/tournament/{tournamentId}")
    Map<String, Object> getClanEloRank(@PathVariable Long clanId, @PathVariable Long tournamentId);

    @GetMapping("/api/elo-ranking/clan/tournament/{tournamentId}")
    Map<String, Object> getAllClanEloRanks(@PathVariable Long tournamentId);
}
