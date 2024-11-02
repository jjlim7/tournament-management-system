package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByTournamentId(Long tournamentId);

    boolean existsByTournamentId(long tournamentId);

    List<Long> findPlayerIdsByGameId(long gameId);

    List<Long> findClanIdsByGameId(long gameId);
}
