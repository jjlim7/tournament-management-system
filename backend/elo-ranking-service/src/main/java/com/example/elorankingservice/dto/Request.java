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
    public static class CreateClanWarGameScore {
        Long winnerClanId;
        Long loserClanId;
        List<PlayerGameScore> winnerRawPlayerGameScores;
        List<PlayerGameScore> loserRawPlayerGameScores;
        Long tournamentId;
        Long gameId;
    }

    @Getter
    public static class CreateNewClanEloRank {
        Long clanId;
        Long tournamentId;
    }

    @Getter
    public static class CreateNewPlayerEloRank {
        Long playerId;
        Long tournamentId;

    }
}
