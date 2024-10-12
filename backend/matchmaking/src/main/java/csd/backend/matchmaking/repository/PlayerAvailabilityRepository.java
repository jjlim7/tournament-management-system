package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.PlayerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayerAvailabilityRepository
  extends JpaRepository<PlayerAvailability, Long> {

    List<PlayerAvailability> getPlayerAvailabilitiesByPlayerId(long playerId);
    List<PlayerAvailability> getPlayerAvailabilitiesByTournamentId(long tournamentId);
    Optional<PlayerAvailability> findByPlayerIdAndTournamentIdAndStartTime(Long playerId, Long tournamentId, OffsetDateTime startTime);

    List<PlayerAvailability> findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(long tournamentId);
}
