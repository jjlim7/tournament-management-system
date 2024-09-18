package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.service.EloRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elo-ranking")
public class EloRankingController {

    private final EloRankingService eloRankingService;

    @Autowired
    public EloRankingController(EloRankingService eloRankingService) {
        this.eloRankingService = eloRankingService;
    }

    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<PlayerEloRank> getPlayerEloRank(@PathVariable Long playerId, @PathVariable Long tournamentId) {
        Optional<PlayerEloRank> playerEloRank = eloRankingService.retrievePlayerEloRank(playerId, tournamentId);
        return playerEloRank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<ClanEloRank> getClanEloRank(@PathVariable Long clanId, @PathVariable Long tournamentId) {
        Optional<ClanEloRank> clanEloRank = eloRankingService.retrieveClanEloRank(clanId, tournamentId);
        return clanEloRank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/clan")
    public ResponseEntity<ClanEloRank> createClanEloRank(@RequestBody Request.CreateNewClanEloRank newClanEloRankRequest) {
        ClanEloRank newEloRank = eloRankingService.createNewClanEloRanking(newClanEloRankRequest.getClanId(), newClanEloRankRequest.getTournamentId());
        return new ResponseEntity<>(newEloRank, HttpStatus.CREATED);
    }

    @PostMapping("/player")
    public ResponseEntity<PlayerEloRank> createPlayerEloRank(@RequestBody Request.CreateNewPlayerEloRank newPlayerEloRankRequest) {
        PlayerEloRank newEloRank = eloRankingService.createNewPlayerEloRanking(newPlayerEloRankRequest.getPlayerId(), newPlayerEloRankRequest.getTournamentId());
        return new ResponseEntity<>(newEloRank, HttpStatus.CREATED);
    }

    @PostMapping("/battle-royale")
    public ResponseEntity<List<PlayerEloRank>> processBattleRoyaleResults(@RequestBody Request.CreateBattleRoyalePlayerGameScore processResultRequest) throws Exception {
        List<PlayerEloRank> finalResult = eloRankingService.processUpdateBattleRoyaleResults(processResultRequest.getRawPlayerGameScores());
        return new ResponseEntity<>(finalResult, HttpStatus.CREATED);
    }

    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanEloRank>> processClanWarResults(@RequestBody Request.CreateClanWarGameScore processResultRequest) throws Exception {
        List<ClanEloRank> finalResult = eloRankingService.processUpdateClanWarResults(processResultRequest.getWinnerRawPlayerGameScores(), processResultRequest.getLoserRawPlayerGameScores());
        return new ResponseEntity<>(finalResult, HttpStatus.CREATED);
    }


}
