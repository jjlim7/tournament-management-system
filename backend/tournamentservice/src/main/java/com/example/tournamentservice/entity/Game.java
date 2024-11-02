package com.example.tournamentservice.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Game name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "player_capacity", nullable = false)
    private int playerCapacity;

    @ElementCollection
    @CollectionTable(name = "players", joinColumns = @JoinColumn(name = "game_id"))
    private List<Long> playerIds;

    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne // Many games can belong to one tournament
    @JoinColumn(name = "tournament_id", nullable = false) // Foreign key to tournament
    private Tournament tournament;
}

