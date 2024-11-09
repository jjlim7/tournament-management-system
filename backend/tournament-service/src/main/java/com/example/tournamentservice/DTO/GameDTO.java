package com.example.tournamentservice.DTO;

import java.time.OffsetDateTime;
import java.util.List;

import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.entity.Tournament.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {

    private Long id; // Assuming the Game has an ID
    private Long tournamentId; // ID of the associated tournament
    private List<Long> playerIds; // List of player IDs
    private OffsetDateTime startTime; // Start time of the game
    private OffsetDateTime endTime; // End time of the game
    private GameMode gameMode; // Enum for the game mode
    private Status gameStatus; // Enum for the game status

}