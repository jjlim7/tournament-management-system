package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.ClanAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ClanAvailabilityRepository extends JpaRepository<ClanAvailability, Long> {

    List<ClanAvailability> findClanAvailabilitiesByTournamentId(long tournamentId);

    Optional<ClanAvailability> findByClanIdAndTournamentIdAndStartTime(Long clanId, Long tournamentId, OffsetDateTime startTime);

    List<ClanAvailability> getClanAvailabilitiesByClanId(long clanId);

    List<ClanAvailability> getClanAvailabilitiesByTournamentId(long tournamentId);

    List<ClanAvailability> getClanAvailabilitiesByClanIdAndTournamentId(long clanId, long tournamentId);
}
