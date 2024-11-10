package csd.backend.matchmaking.feigndto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public enum GameMode {
        BATTLE_ROYALE,
        CLAN_WAR
    }

    private long tournament_id;

    private String name;

    private String description;

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private int playerCapacity;

    private Status status; // Consider using a String or Enum based on your needs

    private GameMode gameMode; // Consider using a String or Enum based on your needs

    private Long adminId;

    // You can include playerIds if needed, or exclude it based on your requirements
    private List<Long> playerIds;
}