package csd.backend.matchmaking.services;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClanAvailabilityService {

    @Autowired
    private ClanAvailabilityRepository repository;

    // Create Clan Availability
    public ClanAvailability createClanAvailability(ClanAvailability availability) {
        Optional<ClanAvailability> exists = repository.findByClanIdAndPlayerIdAndTournamentIdAndStartTime(
                availability.getClanId(),
                availability.getPlayerId(),
                availability.getTournamentId(),
                availability.getStartTime()
        );
        return exists.orElseGet(() -> repository.save(availability));
    }

    // Bulk Create Clan Availabilities
    public List<ClanAvailability> bulkCreateClanAvailabilities(List<ClanAvailability> clanAvailabilities) {
        return repository.saveAll(clanAvailabilities);
    }

    // Get Clan Availabilities by Clan ID
    public List<ClanAvailability> getClanAvailabilitiesByClanId(long clanId) {
        System.out.println("getClanAvailabilitiesByClanId");
        return repository.getClanAvailabilitiesByClanId(clanId);
    }

    // Get Clan Availabilities by Tournament ID
    public List<ClanAvailability> getClanAvailabilitiesByTournamentId(long tournamentId) {
        System.out.println("getClanAvailabilitiesByTournamentId");
        return repository.getClanAvailabilitiesByTournamentId(tournamentId);
    }

    // Get Clan Availabilities by Clan ID and Tournament ID
    public List<ClanAvailability> getClanAvailabilitiesByClanAndTournament(long clanId, long tournamentId) {
        System.out.println("getClanAvailabilitiesByClanAndTournament");
        return repository.getClanAvailabilitiesByClanIdAndTournamentId(clanId, tournamentId);
    }

    // Update Clan Availability (if needed)
    public ClanAvailability updateClanAvailability(Long id, ClanAvailability updatedAvailability) {
        Optional<ClanAvailability> existingAvailability = repository.findById(id);
        if (existingAvailability.isPresent()) {
            ClanAvailability availability = existingAvailability.get();
            availability.setStartTime(updatedAvailability.getStartTime());
            availability.setEndTime(updatedAvailability.getEndTime());
            availability.setAvailable(updatedAvailability.isAvailable());
            return repository.save(availability);
        }
        return null; // Or throw an exception for "not found"
    }

    // Delete Clan Availability
    public void deleteClanAvailability(Long id) {
        repository.deleteById(id);
    }

    public void deleteAvailability(long id) {
        repository.deleteById(id);
    }

    public boolean existsByClanIdAndTournamentIdAndTimeRange(long playerId, long tournamentId, OffsetDateTime start, OffsetDateTime end) {
        return repository.existsByClanIdAndTournamentIdAndTimeRange(playerId, tournamentId, start, end);
    }

    public boolean existsByClanIdAndPlayerIdAndTournamentIdAndTimeRange(long clanId, Long playerId, long tournamentId, OffsetDateTime start, OffsetDateTime end) {
        return repository.existsByClanIdAndPlayerIdAndTournamentIdAndTimeRange(clanId, playerId, tournamentId, start, end);
    }
}