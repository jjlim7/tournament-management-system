package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.ClanGameScore;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.repository.ClanGameScoreRepository;
import com.example.elorankingservice.repository.PlayerGameScoreRepository;

import java.util.List;

public class GameScoreService {
    // for player and clan game score
    private final ClanGameScoreRepository clanGameScoreRepository;
    private final PlayerGameScoreRepository playerGameScoreRepository;

    public GameScoreService(ClanGameScoreRepository clanGameScoreRepository, PlayerGameScoreRepository playerGameScoreRepository) {
        this.clanGameScoreRepository = clanGameScoreRepository;
        this.playerGameScoreRepository = playerGameScoreRepository;
    }

    public List<PlayerGameScore> storeAllPlayerGameScore(List<PlayerGameScore> playersGameScore) {
        return playerGameScoreRepository.saveAll(playersGameScore);
    }


    public ClanGameScore storeClanGameScore(Long gameId, Long clanId, List<PlayerGameScore> playersGameScore, boolean isWinner)  {
        ClanGameScore clanGameScore = new ClanGameScore(gameId, clanId, 0, playersGameScore);
        if (isWinner) {
            clanGameScore.setResult(1);
        }
        return clanGameScoreRepository.save(clanGameScore);
    }

    // Get all the clan game scores for the tournament
    public List<ClanGameScore> retrieveClanGameScoresForTournament(Long tournamentId, Long clanId) {
        return clanGameScoreRepository.findByClanEloRank_TournamentIdAndClanId(tournamentId, clanId);
    }

    public List<PlayerGameScore> retrievePlayerGameScoresForTournament(Long tournamentId, Long playerId) {
        return playerGameScoreRepository.findByPlayerEloRank_TournamentIdAndPlayerId(tournamentId, playerId);
    }
}

