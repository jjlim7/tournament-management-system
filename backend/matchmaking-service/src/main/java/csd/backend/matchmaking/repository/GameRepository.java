package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByTournamentId(Long tournamentId);

    boolean existsByTournamentId(long tournamentId);

    List<Long> findPlayerIdsByGameId(long gameId);

    List<Long> findClanIdsByGameId(long gameId);

    @Query("SELECT g FROM Game g WHERE :playerId MEMBER OF g.playerIds AND g.startTime > :currentDateTime")
    List<Game> findUpcomingGamesByPlayerId(@Param("playerId") long playerId, @Param("currentDateTime") OffsetDateTime currentDateTime);

    @Query("SELECT g FROM Game g WHERE :clanId MEMBER OF g.clanIds AND g.startTime > :currentDateTime")
    List<Game> findUpcomingGamesByClanId(@Param("clanId") long clanId, @Param("currentDateTime") OffsetDateTime currentDateTime);

    @Query("SELECT g FROM Game g WHERE :playerId MEMBER OF g.playerIds")
    List<Game> findGamesByPlayerId(@Param("playerId") long playerId);

    @Query("SELECT g FROM Game g WHERE :clanId MEMBER OF g.clanIds")
    List<Game> findGamesByClanId(@Param("clanId") long clanId);
}
