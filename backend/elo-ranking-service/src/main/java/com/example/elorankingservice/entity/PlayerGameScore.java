package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.time.Duration;
import java.util.Map;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerGameScore extends GameScore {

    public enum GameMode {
        BATTLE_ROYALE,
        CLAN_WAR,
    }

    public enum Role {
        DEFAULT,
        DAMAGE_DEALER,
        HEALER,
        TANK,
    }


    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "game_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @Column(name = "role", nullable = true)
    private Role role;

    // General Metrics
    @Column(name = "kills", nullable = false)
    private int kills;
    
    @Column(name = "deaths", nullable = false)
    private int deaths;
    
    @Column(name = "placement", nullable = false)
    private double placement;
    
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

    // No-Arg Constructor (required by JPA)
    protected PlayerGameScore() {
        super(null); // Pass null for GameScore since it's abstract and doesn't hold logic in the default constructor
    }

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

    public double getHealingDonePerSecond() {
        return (double) healing_done / (double) survival_time.toSeconds();
    }

    public double getHeadshotAccuracy() {
        return (double) headshots / (double) shots_fired;
    }

    public double getDamageDonePerSecond() {
        return (double) damage_done / (double) survival_time.toSeconds();
    }

    public double getDamageTanked() {
        return (double) damage_taken / (double) survival_time.toSeconds();
    }

    public double getRolePerformanceScore(Map<String, Double> roleWeightageConfig) {
        double rps = 0.0;

        for (Map.Entry<String, Double> entry : roleWeightageConfig.entrySet()) {
            String metric = entry.getKey();
            double weight = entry.getValue();

            switch (metric) {
                case "kdr":
                    rps += weight * getKillDeathRatio();
                    break;
                case "acc":
                    rps += weight * getAccuracy();
                    break;
                case "healing":
                    rps += weight * getHealingDonePerSecond();
                    break;
                case "tanked":
                    rps += weight * getDamageTanked();
                    break;
                case "effective_dmg":
                    rps += weight * getEffectiveDamage();
                    break;
                case "headshot_acc":
                    rps += weight * getHeadshotAccuracy();
                    break;
                case "dps":
                    rps += weight * getDamageDonePerSecond();
                    break;
                case "revives":
                    rps += weight * getRevives();
                    break;
                case "assists":
                    rps += weight * getAssists();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown metric " + metric);
            }
        }

        return rps;
    }
}
