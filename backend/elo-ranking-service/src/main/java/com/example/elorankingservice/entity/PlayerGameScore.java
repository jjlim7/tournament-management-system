package com.example.elorankingservice.entity;
import jakarta.persistence.Entity;
import java.time.Duration;

@Entity
public class PlayerGameScore extends GameScore {

    private Long playerId;

    // General Metrics
    private int kills;             // General, also important for damage dealers
    private int deaths;            // General
    private float placement;       // General (placement in Battle Royale games)
    private Duration survival_time; // General
    private double distance_traveled; // General (mobility and positioning)
    private int shots_fired;       // General (accuracy), important for damage dealers
    private int shots_hit;         // General (accuracy), important for damage dealers
    private int longest_kill_streak;

    // Healer-Specific Metrics
    private int healing_done;      // Healer
    private int revives;           // Healer

    // Damage Dealer-Specific Metrics
    private int damage_done;       // Damage Dealer
    private int headshots;         // Damage Dealer

    // Tank-Specific Metrics
    private int damage_taken;      // Tank
    private int damage_mitigated;  // Tank
    private int assists;           // Tank, General

    public PlayerGameScore(
            Long playerId,
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
