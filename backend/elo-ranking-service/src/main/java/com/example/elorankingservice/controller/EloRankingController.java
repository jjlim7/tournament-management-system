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

@RestController
@RequestMapping("/api/elo-ranking")
public class EloRankingController {

    private final EloRankingService eloRankingService;

    @Autowired
    public EloRankingController(EloRankingService eloRankingService) {
        this.eloRankingService = eloRankingService;
    }

    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<PlayerEloRank> getPlayerEloRank(
            @PathVariable Long playerId,
            @PathVariable Long tournamentId) {

        return eloRankingService.retrievePlayerEloRank(playerId, tournamentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<ClanEloRank> getClanEloRank(
            @PathVariable Long clanId,
            @PathVariable Long tournamentId) {

        return eloRankingService.retrieveClanEloRank(clanId, tournamentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/clan")
    public ResponseEntity<ClanEloRank> createClanEloRank(
            @RequestBody Request.CreateNewClanEloRank newClanEloRankRequest) {


        ClanEloRank newEloRank = eloRankingService.createNewClanEloRanking(
                newClanEloRankRequest.getClanId(),
                newClanEloRankRequest.getTournamentId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newEloRank);
    }

    @PostMapping("/player")
    public ResponseEntity<PlayerEloRank> createPlayerEloRank(
            @RequestBody Request.CreateNewPlayerEloRank newPlayerEloRankRequest) {

        PlayerEloRank newEloRank = eloRankingService.createNewPlayerEloRanking(
                newPlayerEloRankRequest.getPlayerId(),
                newPlayerEloRankRequest.getTournamentId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newEloRank);
    }

    @PostMapping("/battle-royale")
    public ResponseEntity<List<PlayerEloRank>> processBattleRoyaleResults(
            @RequestBody Request.CreateBattleRoyalePlayerGameScore processResultRequest) throws Exception {

        // Check if the request is null or doesn't contain player game scores
        if (processResultRequest == null || processResultRequest.getRawPlayerGameScores() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<PlayerEloRank> finalResult = eloRankingService.processUpdateBattleRoyaleResults(
                processResultRequest.getRawPlayerGameScores());

        return ResponseEntity.status(HttpStatus.CREATED).body(finalResult);
    }

    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanEloRank>> processClanWarResults(
            @RequestBody Request.CreateClanWarGameScore processResultRequest) throws Exception {

        if (processResultRequest == null || processResultRequest.getWinnerRawPlayerGameScores().isEmpty() || processResultRequest.getLoserRawPlayerGameScores().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<ClanEloRank> finalResult = eloRankingService.processUpdateClanWarResults(
                processResultRequest.getWinnerRawPlayerGameScores(),
                processResultRequest.getLoserRawPlayerGameScores());

        return ResponseEntity.status(HttpStatus.CREATED).body(finalResult);
    }
}
