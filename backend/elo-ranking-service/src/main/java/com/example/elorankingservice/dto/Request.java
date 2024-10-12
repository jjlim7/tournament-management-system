package com.example.elorankingservice.dto;


import com.example.elorankingservice.entity.PlayerGameScore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class Request {

    @Getter
    @Setter
    public static class CreateBattleRoyalePlayerGameScore {
        List<PlayerGameScore> rawPlayerGameScores;
    }

    @Getter
    @Setter
    public static class CreateClanWarGameScore {
        Long winnerClanId;
        Long loserClanId;
        List<PlayerGameScore> winnerRawPlayerGameScores;
        List<PlayerGameScore> loserRawPlayerGameScores;
        Long tournamentId;
        Long gameId;
    }

    @Getter
    @Setter
    public static class SimulateBattleRoyale {
        List<Long> playerIds;
        Long tournamentId;
        Long gameId;
    }

    @Getter
    @Setter
    public static class SimulateClanWar {
        Long tournamentId;
        Long gameId;
        Map<Long, List<Long>> clanPlayerIds;
    }

    @Getter
    @Setter
    public static class GetSelectedPlayerEloRanks {
        List<Long> playerIds;
        Long tournamentId;
    }

    @Getter
    @Setter
    public static class GetSelectedClanEloRanks {
        List<Long> clanIds;
        Long tournamentId;
    }

    @Getter
    @Setter
    public static class CreateNewClanEloRank {
        Long clanId;
        Long tournamentId;
    }

    @Getter
    @Setter
    public static class CreateNewPlayerEloRank {
        Long playerId;
        Long tournamentId;
    }

    @Getter
    @Setter
    public static class BulkCreateClanEloRank {
        List<Long> clanIds;
        Long tournamentId;
    }

    @Getter
    @Setter
    public static class BulkCreatePlayerEloRank {
        List<Long> playerIds;
        Long tournamentId;
    }
}
