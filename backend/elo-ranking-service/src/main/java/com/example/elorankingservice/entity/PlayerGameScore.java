package com.example.elorankingservice.entity;


import jakarta.persistence.Entity;

import java.math.BigInteger;
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
    private int critical_hits;     // General, also useful for damage dealers
    private int items_used;        // General (resource management)

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
}
