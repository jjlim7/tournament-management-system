package csd.backend.matchmaking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGameScore {

    private Long playerId;
    private String gameMode; // Consider using an Enum if needed
    private String role; // Consider using an Enum if needed
    private int kills;
    private int deaths;
    private double placement;
    private Duration survivalTime;
    private double distanceTraveled;
    private int shotsFired;
    private int shotsHit;
    private int longestKillStreak;
    private int healingDone;
    private int revives;
    private int damageDone;
    private int headshots;
    private int damageTaken;
    private int damageMitigated;
    private int assists;
    private Long playerEloRankId; // Assuming you want to keep a reference to PlayerEloRank
}