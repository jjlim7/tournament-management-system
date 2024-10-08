package csd.backend.matchmaking.services;

import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {
    @Autowired
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    private static final int MIN_PLAYERS_PER_GAME = 3;
    private static final int MAX_PLAYERS_PER_GAME = 20;

    public List<Game> scheduleGames(long tournamentId) throws Exception {
        List<Game> games = gameService.getGamesByTournament(tournamentId);
        Game.GameMode gameMode = games.get(0).getGameMode();
        if (!games.isEmpty()) {
            System.out.printf("Games already scheduled for tournament %s%n", tournamentId);
            return games;
        }

        List<PlayerAvailability> availabilities = playerAvailabilityRepository.findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(tournamentId);
        if (availabilities.isEmpty()) {
            throw new Exception("No player availabilities found for tournament %s".formatted(tournamentId));
        }

        List<Game> scheduledGames = new ArrayList<>();
        Map<OffsetDateTime, List<PlayerAvailability>> availabilityMap = groupAvailabilitiesByStartTime(availabilities);

        for (Map.Entry<OffsetDateTime, List<PlayerAvailability>> entry : availabilityMap.entrySet()) {
            OffsetDateTime startTime = entry.getKey();
            List<PlayerAvailability> availablePlayersSlot = entry.getValue();
            OffsetDateTime endTime = availablePlayersSlot.get(0).getEndTime();

            if (availablePlayersSlot.size() >= MIN_PLAYERS_PER_GAME) {
                Game game = createGame(tournamentId, startTime, endTime, gameMode, availablePlayersSlot);
                if (game != null) {
                    scheduledGames.add(game);
                }
            }
        }

        return scheduledGames;
    }

    public Map<OffsetDateTime, List<PlayerAvailability>> groupAvailabilitiesByStartTime(List<PlayerAvailability> availabilities) {
        return availabilities.stream()
                .filter(PlayerAvailability::isAvailable)
                .collect(Collectors.groupingBy(PlayerAvailability::getStartTime));
    }

    public Game createGame(long tournamentId, OffsetDateTime startTime, OffsetDateTime endTime, Game.GameMode gameMode, List<PlayerAvailability> availablePlayers) {
        List<Long> playerIds = availablePlayers.stream()
                .filter(pa -> pa.getStartTime().isEqual(startTime))
                .map(PlayerAvailability::getPlayerId)
                .distinct()
                .limit(MAX_PLAYERS_PER_GAME)
                .collect(Collectors.toList());

        if (playerIds.size() >= MIN_PLAYERS_PER_GAME) {
            return gameService.createGame(tournamentId, playerIds, startTime, endTime, gameMode, Game.GameStatus.SCHEDULED);
        }
        return null;
    }

    public boolean isTournamentAlreadyScheduled(long tournamentId) {
        return gameRepository.existsByTournamentId(tournamentId);
    }
}
