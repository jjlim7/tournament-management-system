package com.example.tournamentservice.controller;

import com.example.tournamentservice.DTO.GameDTO;
import com.example.tournamentservice.entity.Game;
import com.example.tournamentservice.service.GameService;
import com.example.tournamentservice.service.MatchMakingFeignClient;
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

    @Autowired
    private MatchMakingFeignClient matchMakingFeignClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<GameDTO>> getUpcomingGamesByPlayerId(@RequestParam Long playerId) {
        List<GameDTO> upcomingGames = matchMakingFeignClient.getUpcomingGamesByPlayerId(playerId);
        return ResponseEntity.ok(upcomingGames);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDetails) {
        Game updatedGame = gameService.updateGame(id, gameDetails);
        if (updatedGame != null) {
            return ResponseEntity.ok(updatedGame);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}

