package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Response;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game gameDto) {
        Game game = gameService.createGame(
                gameDto.getGameMode(),
                gameDto.getTournamentId(),
                gameDto.getPlayerIds(),
                gameDto.getClanIds(),
                gameDto.getStartTime(),
                gameDto.getEndTime(),
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

    @GetMapping("/upcoming/player")
    public ResponseEntity<List<Game>> getUpcomingGamesByPlayerId(@RequestParam long playerId) {
        List<Game> upcomingGames = gameService.findUpcomingGamesByPlayerId(playerId);
        return new ResponseEntity<>(upcomingGames, HttpStatus.OK);
    }

    @GetMapping("/upcoming/clan")
    public ResponseEntity<List<Game>> getUpcomingGamesByClanId(@RequestParam long clanId) {
        List<Game> upcomingGames = gameService.findUpcomingGamesByClanId(clanId);
        return new ResponseEntity<>(upcomingGames, HttpStatus.OK);
    }

    @GetMapping("/player")
    public ResponseEntity<List<Game>> getGamesByPlayerId(@RequestParam long playerId) {
        List<Game> games = gameService.findGamesByPlayerId(playerId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/clan")
    public ResponseEntity<List<Game>> getGamesByClanId(@RequestParam long clanId) {
        List<Game> games = gameService.findGamesByClanId(clanId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
