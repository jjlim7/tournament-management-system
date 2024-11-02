package com.example.tournamentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tournamentservice.entity.Game;
import com.example.tournamentservice.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game updateGame(Long id, Game gameDetails) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game != null) {
            game.setName(gameDetails.getName());
            game.setPlayerCapacity(gameDetails.getPlayerCapacity());
            game.setPlayerIds(gameDetails.getPlayerIds());
            game.setStatus(gameDetails.getStatus());
            return gameRepository.save(game);
        }
        return null;
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}