package com.example.tournamentservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Tournament {

	public enum GameMode {
		BATTLE_ROYALE,
		CLAN_WAR,
	}

	public enum Status {
		INACTIVE,
		SCHEDULED,
		ACTIVE,
		CANCELLED,
		ENDED,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tournament_id", nullable = false)
	private Long tournament_id;

	@NotNull(message = "Tournament name is needed")
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", length = 1000)
	private String description;

	@NotNull(message = "Start date is required")
	@Column(name = "start_date", nullable = false)
	private OffsetDateTime startDate;

	@NotNull(message = "End date is required")
	@Future(message = "End date must be a future date")
	@Column(name = "end_date", nullable = false)
	private OffsetDateTime endDate;

	@Column(name = "player_capacity", nullable = false)
	private int playerCapacity;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "game_mode", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private GameMode gameMode;

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "tournament_id")
    //private List<Game> gameList;

	@Column(name = "admin_id", nullable = false)
    private Long adminId;

	@ElementCollection
    @CollectionTable(name = "tournament_players", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "player_id")
    private List<Long> playerIds;

}


