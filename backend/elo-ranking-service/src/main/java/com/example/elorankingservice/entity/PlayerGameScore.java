package com.example.elorankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "player_game_score", schema = "elo-ranking")
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
    private Duration survivalTime;

    @Column(name = "distance_traveled", nullable = false)
    private double distanceTraveled;

    @Column(name = "shots_fired", nullable = false)
    private int shotsFired;

    @Column(name = "shots_hit", nullable = false)
    private int shotsHit;

    @Column(name = "longest_kill_streak", nullable = false)
    private int longestKillStreak;

    // Healer-Specific Metrics
    @Column(name = "healing_done", nullable = false)
    private int healingDone;

    @Column(name = "revives", nullable = false)
    private int revives;

    // Damage Dealer-Specific Metrics
    @Column(name = "damage_done", nullable = false)
    private int damageDone;

    @Column(name = "headshots", nullable = false)
    private int headshots;

    // Tank-Specific Metrics
    @Column(name = "damage_taken", nullable = false)
    private int damageTaken;

    @Column(name = "damage_mitigated", nullable = false)
    private int damageMitigated;

    @Column(name = "assists", nullable = false)
    private int assists;

    // Foreign Key Reference to PlayerEloRank
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_elo_rank_id", referencedColumnName = "id", nullable = false) // Use the correct column name
    private PlayerEloRank playerEloRank;


    public PlayerGameScore() {
        super(null, null);
    }
    // Constructor (if needed)
    public  PlayerGameScore(
            Long playerId,
            Long gameId,
            Long tournamentId,
            Role role,
            GameMode gameMode,
            int kills,
            int deaths,
            float placement,
            Duration survivalTime,
            double distanceTraveled,
            int shotsFired,
            int shotsHit,
            int healingDone,
            int revives,
            int damageDone,
            int headshots,
            int damageTaken,
            int damageMitigated,
            int assists,
            int longestKillStreak
    ) {
        super(gameId, tournamentId);
        this.playerId = playerId;
        this.kills = kills;
        this.deaths = deaths;
        this.role = role;
        this.gameMode = gameMode;
        this.placement = placement;
        this.survivalTime = survivalTime;
        this.distanceTraveled = distanceTraveled;
        this.shotsFired = shotsFired;
        this.shotsHit = shotsHit;
        this.healingDone = healingDone;
        this.revives = revives;
        this.damageDone = damageDone;
        this.headshots = headshots;
        this.damageTaken = damageTaken;
        this.damageMitigated = damageMitigated;
        this.assists = assists;
        this.longestKillStreak = longestKillStreak;
    }


    @JsonIgnore
    public double getKillDeathRatio() {
        // prevent division by zero error
        if (deaths == 0) {
            deaths = 1;
        }
        if (kills == 0) {
            kills = 1;
        }
        return (double) kills / (double) deaths;
    }

    @JsonIgnore
    public double getAccuracy() {
        if (shotsFired == 0) {
            shotsFired = 1;
        }
        if (shotsHit == 0) {
            shotsHit = 1;
        }
        return (double) shotsHit / (double) shotsFired;
    }

    @JsonIgnore
    public double getEffectiveDamage() {
        if (damageDone == 0) {
            damageDone = 1;
        }
        if (damageTaken == 0) {
            damageTaken = 1;
        }
        return (double) damageDone / (double) shotsFired;
    }

    @JsonIgnore
    public double getHealingDonePerSecond() {
        if (healingDone == 0) {
            healingDone = 1;
        }
        return (double) healingDone / (double) survivalTime.toSeconds();
    }
    @JsonIgnore
    public double getHeadshotAccuracy() {
        if (headshots == 0) {
            headshots = 1;
        }
        if (shotsFired == 0) {
            shotsFired = 1;
        }
        return (double) headshots / (double) shotsFired;
    }
    @JsonIgnore
    public double getDamageDonePerSecond() {
        if (damageDone == 0) {
            damageDone = 1;
        }
        return (double) damageDone / (double) survivalTime.toSeconds();
    }
    @JsonIgnore
    public double getDamageTanked() {
        if (damageTaken == 0) {
            damageTaken = 1;
        }
        return (double) damageTaken / (double) survivalTime.toSeconds();
    }
    @JsonIgnore
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

    @Override
    public String toString() {
        return "PlayerGameScore{" +
                "playerId=" + playerId +
                ", gameMode=" + gameMode +
                ", role=" + role +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", placement=" + placement +
                ", survivalTime=" + survivalTime +
                ", distanceTraveled=" + distanceTraveled +
                ", shotsFired=" + shotsFired +
                ", shotsHit=" + shotsHit +
                ", longestKillStreak=" + longestKillStreak +
                ", healingDone=" + healingDone +
                ", revives=" + revives +
                ", damageDone=" + damageDone +
                ", headshots=" + headshots +
                ", damageTaken=" + damageTaken +
                ", damageMitigated=" + damageMitigated +
                ", assists=" + assists +
                ", playerEloRank=" + (playerEloRank != null ? playerEloRank.getId() : "null") + // avoid lazy loading
                '}';
    }
}
