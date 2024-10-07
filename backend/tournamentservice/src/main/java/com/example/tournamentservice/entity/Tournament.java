package com.example.tournamentservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tournaments")
@Data //Generate getter, setter, toString, etc
@NoArgsConstructor
@AllArgsConstructor

public class Tournament {

	public enum GameMode {
		Royale,
		ClanWar,
	}

	public enum Status {
		Rescheduled,
		Cancelled,
		Active,
		Ended,
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
	private Date startDate;

	@NotNull(message = "End date is required")
	@Future(message = "End date must be a future date")
	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@Column(name = "player_capacity", nullable = false)
	private int playerCapacity;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "game_mode", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private GameMode gameMode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Column(name = "Game", nullable = false)
	private List<Game> gameList;

	@Transient
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false)
	private String admin_id;

}


