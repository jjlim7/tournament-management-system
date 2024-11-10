package com.example.tournamentservice.service;

import java.time.OffsetDateTime;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tournamentservice.DTO.ClanEloRankDTO;
import com.example.tournamentservice.DTO.PlayerEloRankDTO;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.entity.Tournament.Status;
import com.example.tournamentservice.exception.TournamentsNotFoundException;
import com.example.tournamentservice.repository.TournamentRepository;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private EloRankingFeignClient eloRankingFeignClient;

    //@Autowired
    //private MatchmakingServiceClient matchmakingClient;



    public Tournament createTournament(Tournament tournament) {
        validateDates(tournament.getStartDate(), tournament.getEndDate());
        validateCapacity(tournament.getPlayerCapacity());

        // Set default values if missing
        if (tournament.getStatus() == null) {
            tournament.setStatus(Status.INACTIVE);
        }
        if (tournament.getGameMode() == null) {
            tournament.setGameMode(GameMode.BATTLE_ROYALE); // Default to 'BATTLE_ROYALE'
        }

        return tournamentRepository.save(tournament);
    }

    public List<Tournament> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll(
            Sort.by(Sort.Direction.ASC, "status").and(Sort.by(Sort.Direction.ASC, "startDate")));
        if (tournaments.isEmpty()) {
            throw new TournamentsNotFoundException("No tournaments found.");
        }
        return tournaments;
    }

    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentsNotFoundException("Tournament with ID " + id + " not found."));
    }

    public Tournament getTournamentByName(String name) {
        return tournamentRepository.findByName(name)
                .orElseThrow(() -> new TournamentsNotFoundException("Tournament with name '" + name + "' not found."));
    }

    public List<Tournament> getTournamentsByDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        List<Tournament> tournaments = tournamentRepository.findByDateRange(startDate, endDate);
        if (tournaments.isEmpty()) {
            throw new TournamentsNotFoundException("No tournaments found between " + startDate + " and " + endDate + ".");
        }
        return tournaments;
    }

    public Tournament updateTournament(Long id, Tournament tournament) {
        Tournament existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        validateEditableStatus(existingTournament);

        // Update fields if provided
        updateFieldIfPresent(existingTournament::setName, tournament.getName());
        updateFieldIfPresent(existingTournament::setDescription, tournament.getDescription());

        validateAndUpdateCapacity(existingTournament, tournament.getPlayerCapacity());
        validateAndUpdateDates(existingTournament, tournament.getStartDate(), tournament.getEndDate());

        return tournamentRepository.save(existingTournament);
    }

    public void deleteTournament(Long id, Long requestingAdminId) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + id));

        if (!tournament.getAdminId().equals(requestingAdminId)) {
            throw new SecurityException("Only the tournament creator can delete this tournament.");
        }

        tournamentRepository.deleteById(id);
    }

    public String joinTournament(Long tournamentId, Long playerId) {
        Tournament tournament = validateAndRetrieveTournament(tournamentId);

        if (tournament.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Player is already registered in this tournament.");
        }

        if (tournament.getPlayerIds().size() >= tournament.getPlayerCapacity()) {
            throw new IllegalArgumentException("Tournament has reached its player capacity.");
        }

        tournament.getPlayerIds().add(playerId);
        tournamentRepository.save(tournament);

        return "Player joined the tournament successfully.";
    }

    public String leaveTournament(long tournamentId, Long playerId) {
        Tournament tournament = validateAndRetrieveTournament(tournamentId);

        if (!tournament.getPlayerIds().remove(playerId)) {
            throw new IllegalArgumentException("Player with ID " + playerId + " is not registered in this tournament.");
        }

        tournamentRepository.save(tournament);
        return "Player with ID " + playerId + " left the tournament successfully.";
    }

    // Helper method for tournament retrieval
    private Tournament validateAndRetrieveTournament(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));
    }

    // Validation Helpers

    private void validateEditableStatus(Tournament tournament) {
        if (tournament.getStatus() == Status.ACTIVE) {
            throw new IllegalStateException("Cannot edit an active tournament");
        }
    }

    private void updateFieldIfPresent(java.util.function.Consumer<String> updater, String value) {
        if (value != null && !value.isEmpty()) {
            updater.accept(value);
        }
    }

    private void validateAndUpdateCapacity(Tournament tournament, int newCapacity) {
        if (newCapacity > 0 && newCapacity >= tournament.getPlayerIds().size()) {
            tournament.setPlayerCapacity(newCapacity);
        } else if (newCapacity > 0) {
            throw new IllegalArgumentException("Player capacity cannot be less than the current number of registered players");
        }
    }

    private void validateAndUpdateDates(Tournament tournament, OffsetDateTime newStartDate, OffsetDateTime newEndDate) {
        if (newStartDate != null && newStartDate.isAfter(OffsetDateTime.now())) {
            tournament.setStartDate(newStartDate);
        }
        if (newEndDate != null) {
            OffsetDateTime startDate = tournament.getStartDate();
            if (startDate != null && (newEndDate.isBefore(startDate) || newEndDate.isEqual(startDate))) {
                throw new IllegalArgumentException("End date cannot be the same as or before the start date");
            }
            tournament.setEndDate(newEndDate);
        }
    }

    private void validateDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("End date cannot be the same as or before the start date");
        }
    }

    private void validateCapacity(int playerCapacity) {
        if (playerCapacity <= 0) {
            throw new IllegalArgumentException("Player capacity must be greater than zero");
        }
    }

    //Feign Client for EloRanking
    public Optional<ClanEloRankDTO> getClanEloRank(Long clanId, Long tournamentId) {
        return eloRankingFeignClient.getClanEloRank(clanId, tournamentId);
    }

    public Optional<PlayerEloRankDTO> getPlayerEloRank(Long playerId, Long tournamentId) {
        return eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId);
    }

    public List<ClanEloRankDTO> getClanEloRanksByTournament(Long tournamentId) {
        return eloRankingFeignClient.getClanEloRanksByTournament(tournamentId);
    }

    public List<PlayerEloRankDTO> getAllPlayerEloRanksByTournament(Long tournamentId) {
        return eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId);
    }

    public List<PlayerEloRankDTO> getSelectedPlayerEloRanksByTournament(List<Long> playerIds, Long tournamentId) {
        return eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds);
    }

    public List<PlayerEloRankDTO> getPlayerEloRanksByRatingRange(Long tournamentId, double minRating, double maxRating) {
        return eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating);
    }

    public List<ClanEloRankDTO> getClanEloRanksByRatingRange(Long tournamentId, double minRating, double maxRating) {
        return eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating);
    }

    public List<Tournament> findUpcomingTournaments() {
        return tournamentRepository.findUpcomingTournaments();
    }

    public boolean updateTournamentStatus(Long tournamentId, Tournament.Status newStatus) {
        OffsetDateTime now = OffsetDateTime.now();
        return tournamentRepository.findById(tournamentId)
                .map(tournament -> {
                    // Add transition validation logic here
                    if (tournament.getStartDate().isBefore(now)) {
                        tournament.setStatus(newStatus);
                        tournamentRepository.save(tournament);
                        return true;
                    } else {
                        return false;
                    }
                })
                .orElse(false);
    }
}
