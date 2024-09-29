package com.example.tournamentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.PositiveOrZero;


@Entity
@Getter
@Setter

public class Tournament {

	public enum GameMode {
		Royale,
		ClanWar,
	}

	public enum status {
		Rescheduled,
		Cancelled,
		Active,
		Ended,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tournament_id", nullable = false)
	private Long tournament_id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@PositiveOrZero(message = "Player count cannot be less than 0")
	@Column(name = "player_capacity", nullable = false)
	private int playerCapacity;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "game_mode", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private GameMode gameMode;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false)
	private User admin_id;
	
	public Tournament(){}

	public Tournament(Long tournament_id, String name, Date startDate, Date endDate, int playerCapacity, boolean status,
			GameMode gameMode, User admin_id) {
		this.tournament_id = tournament_id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.playerCapacity = playerCapacity;
		this.status = status;
		this.gameMode = gameMode;
		this.admin_id = admin_id;
	}

}


