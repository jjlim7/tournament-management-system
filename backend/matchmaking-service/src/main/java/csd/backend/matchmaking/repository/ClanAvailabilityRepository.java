package csd.backend.matchmaking.repository;

import csd.backend.matchmaking.entity.ClanAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ClanAvailabilityRepository extends JpaRepository<ClanAvailability, Long> {

    List<ClanAvailability> findClanAvailabilitiesByTournamentId(long tournamentId);

    Optional<ClanAvailability> findByClanIdAndTournamentIdAndStartTime(Long clanId, Long tournamentId, OffsetDateTime startTime);

    List<ClanAvailability> getClanAvailabilitiesByClanId(long clanId);

    List<ClanAvailability> getClanAvailabilitiesByTournamentId(long tournamentId);

    List<ClanAvailability> getClanAvailabilitiesByClanIdAndTournamentId(long clanId, long tournamentId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ClanAvailability p " +
            "WHERE p.clanId = :clanId AND p.tournamentId = :tournamentId " +
            "AND p.startTime = :startTime AND p.endTime = :endTime")
    boolean existsByClanIdAndTournamentIdAndTimeRange(@Param("clanId") long clanId,
                                                        @Param("tournamentId") long tournamentId,
                                                        @Param("startTime") OffsetDateTime startTime,
                                                        @Param("endTime") OffsetDateTime endTime);
}
