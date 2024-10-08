package csd.backend.matchmaking.dto;

import csd.backend.matchmaking.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

public class Request {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerAvailabilityDto {
        long playerId;
        long tournamentId;
        OffsetDateTime startTime;
        OffsetDateTime endTime;
        boolean isAvailable;
    }
}
