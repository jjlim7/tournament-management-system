package csd.backend.matchmaking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "game", uniqueConstraints={@UniqueConstraint(columnNames={"tournament_id", "start_time"})})
@Table(name = "game", schema = "matchmaking")
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long gameId;

  @Column(name = "tournament_id", nullable = false)
  private long tournamentId;

  @Column(name = "start_time", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private OffsetDateTime startTime;

  @Column(name = "end_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") 
  private OffsetDateTime endTime;

  @ElementCollection
  @CollectionTable(name = "game_players", schema = "matchmaking", joinColumns = @JoinColumn(name = "game_id"))
  @Cascade(value={CascadeType.ALL})
  @Column(name = "player_id")
  private List<Long> playerIds;

  @ElementCollection
  @CollectionTable(name = "game_clans", schema = "matchmaking", joinColumns = @JoinColumn(name = "game_id"))
  @Cascade(value={CascadeType.ALL})
  @Column(name = "clan_id")
  private List<Long> clanIds;

  // Can be either clan_id or player_id
  @Column(name = "winner")
  private Long winner;

  @Column(name = "game_mode", nullable = false)
  @Enumerated(EnumType.STRING)
  private GameMode gameMode;

  @Column(name = "game_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private GameStatus gameStatus;

  public enum GameMode {
    BATTLE_ROYALE,
    CLAN_WAR,
  }

  // Enum for game status
  public enum GameStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
  }

  public Game(long tournamentId, List<Long> playerIds, OffsetDateTime startTime) {
    this.tournamentId = tournamentId;
    this.playerIds = playerIds;
    this.startTime = startTime;
    this.gameStatus = GameStatus.SCHEDULED;
  }

  // Constructor for Battle Royale
  public Game(long tournamentId, List<Long> playerIds, List<Long> clanIds, OffsetDateTime startTime, OffsetDateTime endTime, GameMode gameMode, GameStatus gameStatus) {
    this.tournamentId = tournamentId;
    this.playerIds = playerIds;
    this.clanIds = clanIds;
    this.startTime = startTime;
    this.endTime = endTime;
    this.gameMode = gameMode;
    this.gameStatus = gameStatus;
  }
}
