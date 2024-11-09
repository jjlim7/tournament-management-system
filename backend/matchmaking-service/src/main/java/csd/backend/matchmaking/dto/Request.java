package csd.backend.matchmaking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

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

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BulkCreatePlayerAvailabilityByTimeRangeDto {

    long playerId;
    long tournamentId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    int interval;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateClanAvailabilityDto {

    long clanId;
    long tournamentId;
    String startTime;
    String endTime;
    boolean isAvailable;
  }

  @Getter
  @Setter
  public static class GetSelectedPlayerEloRanks {
    List<Long> playerIds;
    Long tournamentId;
  }
}
