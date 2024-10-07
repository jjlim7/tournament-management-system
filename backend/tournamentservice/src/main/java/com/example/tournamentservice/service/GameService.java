package com.example.tournamentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tournamentservice.entity.Game;
import com.example.tournamentservice.repository.GameRepository;
import java.util.List;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;

    public List<Game> findAll(){
        return gameRepository.findAll();
    }

    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow();
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}
