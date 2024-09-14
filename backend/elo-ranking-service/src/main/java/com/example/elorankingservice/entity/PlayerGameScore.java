package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.time.Duration;

@Entity
public class PlayerGameScore extends GameScore {
    
    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "game_mode", nullable = false)
    private GameMode gameMode;

    @Column(name = "role", nullable = true)
    private Role role;

    // General Metrics
    @Column(name = "kills", nullable = false)
    private int kills; // General, also important for damage dealers
    
    @Column(name = "deaths", nullable = false)
    private int deaths;            // General
    
    @Column(name = "placement", nullable = true) // nullable only for clan wars
    private float placement;       // General (placement in Battle Royale games)
    
    @Column(name = "survival_time", nullable = false)
    private Duration survival_time; // General
    
    @Column(name = "distance_traveled", nullable = false)
    private double distance_traveled; // General (mobility and positioning)
    
    @Column(name = "shots_fired", nullable = false)
    private int shots_fired;       // General (accuracy), important for damage dealers
    
    @Column(name = "shots_hit", nullable = false)
    private int shots_hit;         // General (accuracy), important for damage dealers
    
    @Column(name = "longest_kill_streak", nullable = false)
    private int longest_kill_streak;

    // Healer-Specific Metrics
    @Column(name = "healing_done", nullable = false)
    private int healing_done;      // Healer
        
    @Column(name = "revives", nullable = false)
    private int revives;           // Healer

    // Damage Dealer-Specific Metrics
    @Column(name = "damage_done", nullable = false)
    private int damage_done;       // Damage Dealer
        
    @Column(name = "headshots", nullable = false)
    private int headshots;         // Damage Dealer

    // Tank-Specific Metrics
    @Column(name = "damage_taken", nullable = false)
    private int damage_taken;      // Tank
    
    @Column(name = "damage_mitigated", nullable = false)
    private int damage_mitigated; // Tank

    @Column(name = "assists", nullable = false)
    private int assists;           // Tank, General

    public PlayerGameScore(
            Long playerId,
            Long gameId,
            int kills,
            int deaths,
            float placement,
            Duration survival_time,
            double distance_traveled,
            int shots_fired,
            int shots_hit,
            int healing_done,
            int revives,
            int damage_done,
            int headshots,
            int damage_taken,
            int damage_mitigated,
            int assists,
            int longest_kill_streak
    ) {
        super(gameId);
        this.playerId = playerId;
        this.kills = kills;
        this.deaths = deaths;
        this.placement = placement;
        this.survival_time = survival_time;
        this.distance_traveled = distance_traveled;
        this.shots_fired = shots_fired;
        this.shots_hit = shots_hit;
        this.healing_done = healing_done;
        this.revives = revives;
        this.damage_done = damage_done;
        this.headshots = headshots;
        this.damage_taken = damage_taken;
        this.damage_mitigated = damage_mitigated;
        this.assists = assists;
        this.longest_kill_streak = longest_kill_streak;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public float getPlacement() {
        return placement;
    }

    public Duration getSurvivalTime() {
        return survival_time;
    }
    
    public double getDistanceTraveled() {
        return distance_traveled;
    }

    public int getShotsFired() {
        return shots_fired;
    }
    
    public int getShotsHit() {
        return shots_hit;
    }

    public int getHealingDone() {
        return healing_done;
    }
    
    public int getRevives() {
        return revives;
    }

    public int getLongestKillStreak() {
        return longest_kill_streak;
    }

    public int getDamageDone() {
        return damage_done;
    }
    
    public int getHeadshots() {
        return headshots;
    }

    public int getDamageMitigated() {
        return damage_mitigated;
    }

    public int getAssists() {
        return assists;
    }

    public int getDamageTaken() {
        return damage_taken;
    }

    public double getKillDeathRatio() {
        return (double) kills / (double) deaths;
    }
    
    public double getAccuracy() {
        return (double) shots_hit / (double) shots_fired;
    }

    // general but more for damage dealers
    public double getEffectiveDamage() {
        return (double) damage_done / (double) shots_fired;
    }

    // for healers
    public double getHealingDonePerMinute() {
        return (double) healing_done / (double) survival_time.toMinutes();
    }

    // for tanks
    public double getDamageMitigatedPerMinute() {
        return (double) damage_mitigated / (double) survival_time.toMinutes();
    }
    
    // for damage dealers
    public double getHeadshotAccuracy() {
        return (double) headshots / (double) shots_fired;
    }

    // for damage dealers
    public double getDamageDonePerMinute() {
        return (double) damage_done / (double) survival_time.toMinutes();
    }
}
