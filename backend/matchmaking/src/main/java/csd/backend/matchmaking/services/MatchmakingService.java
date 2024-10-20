package csd.backend.matchmaking.services;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.EloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static csd.backend.matchmaking.entity.Game.GameMode.BATTLE_ROYALE;

@Service
public class MatchmakingService {
    @Autowired
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    @Autowired
    private ClanAvailabilityRepository clanAvailabilityRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private EloRankingClient eloRankingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int MIN_PLAYERS_PER_GAME = 3;
    private static final int MAX_PLAYERS_PER_GAME = 20;

    public List<Game> scheduleGames (long tournamentId, Game.GameMode gameMode) throws Exception {
        switch (gameMode) {
            case BATTLE_ROYALE: {
                return scheduleBattleRoyaleGames(tournamentId);
            }
            case CLAN_WAR: {
                return scheduleClanWarGames(tournamentId);
            }
        }
        throw new Exception("Invalid gameMode");
    }

    private List<Game> scheduleClanWarGames(long tournamentId) {
        List<ClanAvailability> availabilities = clanAvailabilityRepository.findClanAvailabilitiesByTournamentId(tournamentId);
        List<Long> clanIds = availabilities.stream().map(ClanAvailability::getClanId).toList();

        Map<Long, EloRank> playerEloRankMap = getPlayerEloRankMap(clanIds, tournamentId);

        return null;
    }

    public List<Game> scheduleBattleRoyaleGames(long tournamentId) throws Exception {
        List<PlayerAvailability> availabilities = playerAvailabilityRepository.findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(tournamentId);
        List<Long> playerIds = availabilities.stream().map(PlayerAvailability::getPlayerId).toList();

        Map<Long, EloRank> playerEloRankMap = getPlayerEloRankMap(playerIds, tournamentId);

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
                // Group players by rank
                Map<String, List<PlayerAvailability>> rankedPlayersMap = groupPlayersByRank(availablePlayersSlot, playerEloRankMap);

                // Create games for each rank group
                for (List<PlayerAvailability> rankedPlayers : rankedPlayersMap.values()) {
                    // Check if there are enough players in this rank group to create a game
                    if (rankedPlayers.size() >= MIN_PLAYERS_PER_GAME) {
                        Game game = createBattleRoyaleGame(tournamentId, startTime, endTime, rankedPlayers);
                        if (game != null) {
                            scheduledGames.add(game);
                        }
                    }
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

    private Map<String, List<PlayerAvailability>> groupPlayersByRank(List<PlayerAvailability> availablePlayers, Map<Long, EloRank> eloRankMap) {
        Map<String, List<PlayerAvailability>> rankedPlayersMap = new HashMap<>();

        // Group players by their rank
        for (PlayerAvailability availability : availablePlayers) {
            Long playerId = availability.getPlayerId();
            String rank = String.valueOf(eloRankMap.get(playerId).getRankThreshold().getRank()); // Get the player's rank

            rankedPlayersMap.computeIfAbsent(rank, k -> new ArrayList<>()).add(availability);
        }

        return rankedPlayersMap; // Returns a map where the key is the rank and the value is a list of players in that rank
    }

    public Game createBattleRoyaleGame(long tournamentId, OffsetDateTime startTime, OffsetDateTime endTime, List<PlayerAvailability> availablePlayers) {
        List<Long> playerIds = availablePlayers.stream()
                .filter(pa -> pa.getStartTime().isEqual(startTime))
                .map(PlayerAvailability::getPlayerId)
                .distinct()
                .limit(MAX_PLAYERS_PER_GAME)
                .collect(Collectors.toList());

        if (playerIds.size() >= MIN_PLAYERS_PER_GAME) {
            return gameService.createBattleRoyaleGame(tournamentId, playerIds, startTime, endTime, Game.GameStatus.SCHEDULED);
        }
        return null;
    }

    public Game createClanWarGame(long tournamentId, OffsetDateTime startTime, OffsetDateTime endTime, List<PlayerAvailability> availablePlayers) {
        List<Long> playerIds = availablePlayers.stream()
                .filter(pa -> pa.getStartTime().isEqual(startTime))
                .map(PlayerAvailability::getPlayerId)
                .distinct()
                .collect(Collectors.toList());

        if (playerIds.size() == 2) {
            return gameService.createClanWarGame(tournamentId, playerIds, startTime, endTime, Game.GameStatus.SCHEDULED);
        }
        return null;
    }

    public boolean isTournamentAlreadyScheduled(long tournamentId) {
        return gameRepository.existsByTournamentId(tournamentId);
    }

    public Map<Long, EloRank> getPlayerEloRankMap(List<Long> playerIds, Long tournamentId) {
        Request.GetSelectedPlayerEloRanks request = new Request.GetSelectedPlayerEloRanks();
        request.setPlayerIds(playerIds);
        request.setTournamentId(tournamentId);

        Map<String, Object> res = eloRankingClient.getSelectedPlayerEloRanks(request);
        Object obj = res.get("playerEloRanks");
        List<PlayerEloRank> playerEloRanks = objectMapper.convertValue(
                obj,
                new TypeReference<>() {
                }
        );

        // Create a map of player ID to Elo Rank for quick access
        Map<Long, EloRank> eloRankMap = new HashMap<>();
        for (PlayerEloRank eloRank : playerEloRanks) {
            eloRankMap.put(eloRank.getPlayerId(), eloRank);
        }
        return eloRankMap;
    }
}
