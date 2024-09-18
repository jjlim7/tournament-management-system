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


    @PostMapping("/battle-royale")
    public ResponseEntity<List<PlayerEloRank>> processBattleRoyaleResults(Request.CreateBattleRoyalePlayerGameScore battleRoyaleResult) throws Exception {
        List<PlayerEloRank> finalResult = eloRankingService.processUpdateBattleRoyaleResults(battleRoyaleResult.getRawPlayerGameScores());
        return new ResponseEntity<>(finalResult, HttpStatus.CREATED);
    }

    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanEloRank>> processClanWarResults(Request.CreateClanWarGameScore clanWarResult) throws Exception {
        List<ClanEloRank> finalResult = eloRankingService.processUpdateClanWarResults(clanWarResult.getWinnerRawPlayerGameScores(), clanWarResult.getLoserRawPlayerGameScores());
        return new ResponseEntity<>(finalResult, HttpStatus.CREATED);
    }
}
