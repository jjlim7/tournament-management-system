package com.example.userservice.feigndto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlayerStats {
    private Long playerId;
    private Long tournamentId;
    private Long totalMatches;
    private GameMode gameMode;

    // General performance metrics
    private int totalKills;
    private int totalDeaths;
    private double avgKillDeathRatio;
    private double avgAccuracy;
    private double avgHeadshotAccuracy;

    // Healing, damage, and survival metrics
    private double avgHealingDonePerSecond;
    private double avgDamageDonePerSecond;
    private double avgDamageTanked;

    // Additional aggregate statistics
    private int totalAssists;
    private int totalRevives;
    private int totalShotsFired;
    private int totalShotsHit;
}
