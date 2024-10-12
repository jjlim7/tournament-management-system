package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.ClanGameScore;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.repository.ClanGameScoreRepository;
import com.example.elorankingservice.repository.PlayerGameScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GameScoreService {
    // for player and clan game score
    private final ClanGameScoreRepository clanGameScoreRepository;
    private final PlayerGameScoreRepository playerGameScoreRepository;
    private final EloRankingService eloRankingService;

    @Autowired
    public GameScoreService(ClanGameScoreRepository clanGameScoreRepository, PlayerGameScoreRepository playerGameScoreRepository, EloRankingService eloRankingService) {
        this.clanGameScoreRepository = clanGameScoreRepository;
        this.playerGameScoreRepository = playerGameScoreRepository;
        this.eloRankingService = eloRankingService;
    }

    public void storeAllPlayerGameScore(List<PlayerGameScore> playersGameScore) {
        playerGameScoreRepository.saveAll(playersGameScore);
    }


    public void storeClanGameScore(Long tournamentId, Long gameId, Long clanId, List<PlayerGameScore> playersGameScore, ClanEloRank currentRank, boolean isWinner)  {
        ClanGameScore clanGameScore = new ClanGameScore(gameId, tournamentId, clanId, 0, playersGameScore);;
        clanGameScore.setClanEloRank(currentRank);
        if (isWinner) {
            clanGameScore.setResult(1);
        }
        System.out.println(clanGameScore);
        clanGameScoreRepository.save(clanGameScore);
    }

    // Get all the clan game scores for the tournament
    public List<ClanGameScore> retrieveClanGameScoresForTournament(Long tournamentId, Long clanId) {
        return clanGameScoreRepository.findByClanEloRank_TournamentIdAndClanId(tournamentId, clanId);
    }

    public List<PlayerGameScore> retrievePlayerGameScoresForTournament(Long tournamentId, Long playerId) {
        return playerGameScoreRepository.findByPlayerEloRank_TournamentIdAndPlayerId(tournamentId, playerId);
    }
}

