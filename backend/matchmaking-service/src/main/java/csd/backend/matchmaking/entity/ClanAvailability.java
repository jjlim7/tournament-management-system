package csd.backend.matchmaking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clan_availability", schema = "matchmaking", uniqueConstraints = {@UniqueConstraint(columnNames={"clan_id", "player_id", "tournament_id", "start_time"})})
public class ClanAvailability {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "clan_id", nullable = false)
  private Long clanId; // Reference to Player in another service

  @Column(name = "player_id", nullable = false)
  private Long playerId; // Reference to Player in another service

  @Column(name = "tournament_id", nullable = false)
  private Long tournamentId; // Reference to Tournament in another service

  @Column(name = "start_time", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private OffsetDateTime startTime;

  @Column(name = "end_time", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private OffsetDateTime endTime;

  @Column(name = "is_available", nullable = false)
  private boolean isAvailable;

  public ClanAvailability(Long clanId, long playerId, long tournamentId, OffsetDateTime startTime, OffsetDateTime endTime, boolean available) {
    this.clanId = clanId;
    this.playerId = playerId;
    this.tournamentId = tournamentId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isAvailable = available;
  }
}
