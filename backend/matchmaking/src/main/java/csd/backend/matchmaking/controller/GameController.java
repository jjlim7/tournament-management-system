package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.dto.Response;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game gameDto) {
        Game game = gameService.createGame(
                gameDto.getTournamentId(),
                gameDto.getPlayerIds(),
                gameDto.getStartTime(),
                gameDto.getEndTime(),
                gameDto.getGameMode(),
                gameDto.getGameStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @PutMapping("/{gameId}/status")
    public ResponseEntity<Game> updateGameStatus(@PathVariable Long gameId, @RequestBody Game.GameStatus gameStatus) {
        Game game = gameService.updateGameStatus(gameId, gameStatus);
        return ResponseEntity.ok(game);
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGamesByTournament(@RequestParam Long tournamentId) {
        List<Game> games = gameService.getGamesByTournament(tournamentId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}/playerIds")
    public ResponseEntity<List<Long>> getPlayerIdsByGame(@PathVariable Long gameId) {
        List<Long> playerIdsByGame = gameService.getPlayerIdsByGame(gameId);
        return ResponseEntity.ok(playerIdsByGame);
    }

    @GetMapping("/{gameId}/clanIds")
    public ResponseEntity<List<Long>> getClanIdsByGame(@PathVariable Long gameId) {
        List<Long> playerIdsByGame = gameService.getPlayerIdsByGame(gameId);
        return ResponseEntity.ok(playerIdsByGame);
    }

    @GetMapping("/{gameId}/playerOrClanIds")
    public ResponseEntity<Response.EntityIdResponse> getPlayerOrClanIdsByGame(@PathVariable Long gameId) {
        Response.EntityIdResponse response = gameService.getGamePlayerOrClanIds(gameId);
        return ResponseEntity.ok(response);
    }
}
