package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import com.example.elorankingservice.service.EloRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elo-ranking")
public class EloRankingController {

    private final EloRankingService eloRankingService;

    @Autowired
    public EloRankingController(EloRankingService eloRankingService, PlayerEloRankRepository playerEloRankRepository) {
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

    @PostMapping("/player/bulk")
    public ResponseEntity<List<PlayerEloRank>> bulkCreatePlayerEloRank(
            @RequestBody Request.BulkCreatePlayerEloRank bulkPlayersEloRankRequest) {
        List<PlayerEloRank> result = new ArrayList<>();
        Long tournamentId = bulkPlayersEloRankRequest.getTournamentId();
        for (Long id : bulkPlayersEloRankRequest.getPlayerIds()) {
            PlayerEloRank eloRank = eloRankingService.createNewPlayerEloRanking(id, tournamentId);
            result.add(eloRank);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PostMapping("/clan/bulk")
    public ResponseEntity<List<ClanEloRank>> bulkCreateClanEloRank(
            @RequestBody Request.BulkCreateClanEloRank bulkClanEloRankRequest) {
        List<ClanEloRank> result = new ArrayList<>();
        Long tournamentId = bulkClanEloRankRequest.getTournamentId();
        for (Long id : bulkClanEloRankRequest.getClanIds()) {
            ClanEloRank eloRank = eloRankingService.createNewClanEloRanking(id, tournamentId);
            result.add(eloRank);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // NOT USED
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

    // NOT USED
    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanEloRank>> processClanWarResults(
            @RequestBody Request.CreateClanWarGameScore processResultRequest) throws Exception {

        if (processResultRequest == null || processResultRequest.getWinnerRawPlayerGameScores().isEmpty() || processResultRequest.getLoserRawPlayerGameScores().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create the map
        Map<Long, List<PlayerGameScore>> winner = new HashMap<>();
        Map<Long, List<PlayerGameScore>> loser = new HashMap<>();

        winner.put(processResultRequest.getWinnerClanId(), processResultRequest.getWinnerRawPlayerGameScores());
        loser.put(processResultRequest.getLoserClanId(), processResultRequest.getLoserRawPlayerGameScores());

        List<ClanEloRank> finalResult = eloRankingService.processUpdateClanWarResults(winner, loser);

        return ResponseEntity.status(HttpStatus.CREATED).body(finalResult);
    }
}
