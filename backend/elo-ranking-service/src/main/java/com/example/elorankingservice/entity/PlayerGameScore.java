package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerGameScore extends GameScore {
    
    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "game_mode", nullable = false)
    private GameMode gameMode;

    @Column(name = "role", nullable = true)
    private Role role;

    // General Metrics
    @Column(name = "kills", nullable = false)
    private int kills;
    
    @Column(name = "deaths", nullable = false)
    private int deaths;
    
    @Column(name = "placement", nullable = true) // nullable only for clan wars
    private float placement;
    
    @Column(name = "survival_time", nullable = false)
    private Duration survival_time;
    
    @Column(name = "distance_traveled", nullable = false)
    private double distance_traveled;
    
    @Column(name = "shots_fired", nullable = false)
    private int shots_fired;
    
    @Column(name = "shots_hit", nullable = false)
    private int shots_hit;
    
    @Column(name = "longest_kill_streak", nullable = false)
    private int longest_kill_streak;

    // Healer-Specific Metrics
    @Column(name = "healing_done", nullable = false)
    private int healing_done;
        
    @Column(name = "revives", nullable = false)
    private int revives;

    // Damage Dealer-Specific Metrics
    @Column(name = "damage_done", nullable = false)
    private int damage_done;
        
    @Column(name = "headshots", nullable = false)
    private int headshots;

    // Tank-Specific Metrics
    @Column(name = "damage_taken", nullable = false)
    private int damage_taken;
    
    @Column(name = "damage_mitigated", nullable = false)
    private int damage_mitigated;

    @Column(name = "assists", nullable = false)
    private int assists;

    // Constructor (if needed)
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

    // Derived or computed methods
    public double getKillDeathRatio() {
        return (double) kills / (double) deaths;
    }
    
    public double getAccuracy() {
        return (double) shots_hit / (double) shots_fired;
    }

    public double getEffectiveDamage() {
        return (double) damage_done / (double) shots_fired;
    }

    public double getHealingDonePerMinute() {
        return (double) healing_done / (double) survival_time.toMinutes();
    }

    public double getDamageMitigatedPerMinute() {
        return (double) damage_mitigated / (double) survival_time.toMinutes();
    }

    public double getHeadshotAccuracy() {
        return (double) headshots / (double) shots_fired;
    }

    public double getDamageDonePerMinute() {
        return (double) damage_done / (double) survival_time.toMinutes();
    }
}
