package csd.backend.matchmaking.dto;

import csd.backend.matchmaking.entity.Game;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Request {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreatePlayerAvailabilityDto {

    long playerId;
    long tournamentId;
    String startTime;
    String endTime;
    boolean isAvailable;
  }
}
