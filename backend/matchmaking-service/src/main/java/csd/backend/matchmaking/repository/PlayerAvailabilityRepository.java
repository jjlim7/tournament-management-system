package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.PlayerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayerAvailabilityRepository
  extends JpaRepository<PlayerAvailability, Long> {

    List<PlayerAvailability> getPlayerAvailabilitiesByPlayerId(long playerId);
    List<PlayerAvailability> getPlayerAvailabilitiesByTournamentId(long tournamentId);
    Optional<PlayerAvailability> findByPlayerIdAndTournamentIdAndStartTime(Long playerId, Long tournamentId, OffsetDateTime startTime);

    List<PlayerAvailability> findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(long tournamentId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PlayerAvailability p " +
            "WHERE p.playerId = :playerId AND p.tournamentId = :tournamentId " +
            "AND p.startTime = :startTime AND p.endTime = :endTime")
    boolean existsByPlayerIdAndTournamentIdAndTimeRange(@Param("playerId") long playerId,
                                                        @Param("tournamentId") long tournamentId,
                                                        @Param("startTime") OffsetDateTime startTime,
                                                        @Param("endTime") OffsetDateTime endTime);

}
