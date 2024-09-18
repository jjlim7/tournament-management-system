package com.example.elorankingservice.dto;


import com.example.elorankingservice.entity.PlayerGameScore;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Request {
    public static class CreateBattleRoyalePlayerGameScore {
        List<PlayerGameScore> rawPlayerGameScores;

        // Getter for rawPlayerGameScores
        public List<PlayerGameScore> getRawPlayerGameScores() {
            return rawPlayerGameScores;
        }
    }

    public static class CreateClanWarGameScore {
        Map<Long, List<PlayerGameScore>> winnerRawPlayerGameScores;
        Map<Long, List<PlayerGameScore>> loserRawPlayerGameScores;

        public Map<Long, List<PlayerGameScore>> getWinnerRawPlayerGameScores() {
            return winnerRawPlayerGameScores;
        }
        public Map<Long, List<PlayerGameScore>> getLoserRawPlayerGameScores() {
            return loserRawPlayerGameScores;
        }
    }
}
