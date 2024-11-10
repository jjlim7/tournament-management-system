package csd.backend.matchmaking.services;

import csd.backend.matchmaking.feignclient.ClanUserClient;
import csd.backend.matchmaking.feigndto.ClanEloRank;
import csd.backend.matchmaking.feigndto.ClanUser;
import csd.backend.matchmaking.feigndto.EloRank;
import csd.backend.matchmaking.feigndto.PlayerEloRank;
import com.fasterxml.jackson.databind.ObjectMapper;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private ClanUserClient clanUserClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int MIN_PLAYERS_PER_GAME = 3;
    private static final int MAX_PLAYERS_PER_GAME = 20;

    @Transactional
    public List<Game> scheduleGames (long tournamentId, String gameMode) throws Exception {
        return switch (gameMode) {
            case "BATTLE_ROYALE" -> scheduleBattleRoyaleGames(tournamentId);
            case "CLAN_WAR" -> scheduleClanWarGames(tournamentId);
            case "CLANWAR" -> scheduleClanWarGames(tournamentId);
            default -> throw new Exception("Invalid gameMode");
        };
    }

    @Transactional
    public List<Game> scheduleClanWarGames(long tournamentId) throws Exception {
        System.out.println("Starting scheduling Clan War games for tournament " + tournamentId);

        List<Game> scheduledGames = new ArrayList<>();

        // Step 1: Fetch Clan Availabilities
        List<ClanAvailability> clanAvailabilities = clanAvailabilityRepository.findClanAvailabilitiesByTournamentId(tournamentId);
        if (clanAvailabilities.isEmpty()) {
            throw new Exception("No clan availabilities found for tournament " + tournamentId);
        }

        // Step 2: Fetch Clans and Their ELO Ranks
        List<Long> clanIds = clanAvailabilities.stream()
                .map(ClanAvailability::getClanId)
                .distinct()
                .toList();

        Map<Long, String> clanEloRankMap = new HashMap<>();
        Map<Long, List<Long>> clanUserIdsMap = new HashMap<>();

        for (Long clanId : clanIds) {
            try {
                // Get the ELO rank for the clan
                Map<String, Object> res = eloRankingClient.getClanEloRank(clanId, tournamentId);
                ClanEloRank eloRank = objectMapper.convertValue(res.get("data"), ClanEloRank.class);
                clanEloRankMap.put(clanId, eloRank.getRankThreshold().getRank().toString());

                // Get the players for each clan using Feign client
                ResponseEntity<List<ClanUser>> response = clanUserClient.getClanUsersByClan(clanId);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<Long> userIds = response.getBody().stream()
                            .map(ClanUser::getUserId)
                            .collect(Collectors.toList());
                    clanUserIdsMap.put(clanId, userIds);
                    System.out.printf("Fetched %d user IDs for Clan %d%n", userIds.size(), clanId);
                } else {
                    System.out.printf("Failed to fetch players for Clan %d: %s%n", clanId, response.getStatusCode());
                }
            } catch (Exception e) {
                System.out.printf("Failed to retrieve ELO rank for clan %d in tournament %d: %s%n", clanId, tournamentId, e.getMessage());
                throw new Exception(String.format("Failed to retrieve ELO rank for clan %d in tournament %d: %s", clanId, tournamentId, e.getMessage()));
            }
        }

        // Step 3: Group Clans by ELO Rank
        Map<String, List<Long>> clansGroupedByElo = clanEloRankMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
        System.out.printf("Clans grouped by ELO rank: %s%n", objectMapper.writeValueAsString(clansGroupedByElo));

        // Step 4: Filter Clan Users Based on Availability
        Map<Long, List<Long>> availableClanUserIdsMap = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : clanUserIdsMap.entrySet()) {
            Long clanId = entry.getKey();
            List<Long> userIds = entry.getValue();

            // Retrieve the Clan's availability periods
            List<ClanAvailability> clanAvailabilitiesByClanId = clanAvailabilities.stream()
                    .filter(ca -> ca.getClanId().equals(clanId))
                    .collect(Collectors.toList());

            // Filter users based on their availability matching clan availability
            List<Long> availableUserIds = userIds.stream()
                    .filter(userId -> {
                        // Fetch the player availability for this user
                        List<PlayerAvailability> userAvailabilities = playerAvailabilityRepository.getPlayerAvailabilitiesByPlayerId(userId);

                        // Check if there's any overlap between user and clan availabilities
                        return clanAvailabilitiesByClanId.stream().anyMatch(clanAvailability ->
                                userAvailabilities.stream().anyMatch(userAvailability ->
                                        isPlayerClanAvailabilityOverlapping(userAvailability, clanAvailability)
                                )
                        );
                    })
                    .collect(Collectors.toList());

            // Store only the available user IDs for this clan
            availableClanUserIdsMap.put(clanId, availableUserIds);
        }

        // Step 5: Group Clan Availabilities by Start Time
        Map<OffsetDateTime, List<ClanAvailability>> availabilitiesGroupedByStartTime =
                clanAvailabilities.stream()
                        .filter(ClanAvailability::isAvailable)
                        .collect(Collectors.groupingBy(ClanAvailability::getStartTime));

        // Step 6: Schedule Games by Pairing Clans Based on ELO and Availability
        for (Map.Entry<String, List<Long>> eloGroupEntry : clansGroupedByElo.entrySet()) {
            String eloRank = eloGroupEntry.getKey();
            List<Long> clansInEloGroup = eloGroupEntry.getValue();

            System.out.printf("Processing Clan War scheduling for ELO Rank: %s with %d clans%n", eloRank, clansInEloGroup.size());

            Collections.shuffle(clansInEloGroup);

            for (OffsetDateTime startTime : availabilitiesGroupedByStartTime.keySet()) {
                List<ClanAvailability> availableClansAtTime = availabilitiesGroupedByStartTime.get(startTime);

                // Filter clans in the ELO group that have available players at this start time
                List<Long> availableClanIds = availableClansAtTime.stream()
                        .map(ClanAvailability::getClanId)
                        .filter(clansInEloGroup::contains)
                        .distinct()
                        .toList();

                for (int i = 0; i < availableClanIds.size() - 1; i += 2) {
                    Long clan1Id = availableClanIds.get(i);
                    Long clan2Id = availableClanIds.get(i + 1);

                    System.out.printf("Attempting to schedule a game between Clan %d and Clan %d at %s%n", clan1Id, clan2Id, startTime);

                    List<Long> clan1UserIds = availableClanUserIdsMap.getOrDefault(clan1Id, Collections.emptyList());
                    List<Long> clan2UserIds = availableClanUserIdsMap.getOrDefault(clan2Id, Collections.emptyList());

                    List<Long> clan1AvailablePlayerIds = availableClansAtTime.stream()
                            .filter(ca -> ca.getClanId().equals(clan1Id))
                            .map(ClanAvailability::getPlayerId)
                            .filter(clan1UserIds::contains)
                            .toList();

                    List<Long> clan2AvailablePlayerIds = availableClansAtTime.stream()
                            .filter(ca -> ca.getClanId().equals(clan2Id))
                            .map(ClanAvailability::getPlayerId)
                            .filter(clan2UserIds::contains)
                            .toList();

                    if (clan1AvailablePlayerIds.isEmpty() || clan2AvailablePlayerIds.isEmpty()) {
                        System.out.printf("Skipping game between Clan %d and Clan %d due to insufficient available players%n", clan1Id, clan2Id);
                        continue;
                    }

                    OffsetDateTime endTime = startTime.plusHours(1);  // Example game duration
                    Game game = gameService.createClanWarGame(
                            tournamentId,
                            List.of(clan1Id, clan2Id),
                            startTime,
                            endTime,
                            Game.GameStatus.SCHEDULED
                    );

                    if (game != null) {
                        scheduledGames.add(game);
                        System.out.printf("Scheduled Clan War game between Clan %d and Clan %d from %s to %s%n",
                                clan1Id, clan2Id, startTime, endTime);
                    }
                }

                if (availableClanIds.size() % 2 != 0) {
                    Long unpairedClanId = availableClanIds.get(availableClanIds.size() - 1);
                    System.out.printf("Odd number of clans available in ELO rank %s at %s. Clan %d will not be paired.%n", eloRank, startTime, unpairedClanId);
                }
            }
        }

        System.out.println("Completed scheduling Clan War games. Total games scheduled: " + scheduledGames.size());
        return scheduledGames;
    }

    @Transactional
    public List<Game> scheduleBattleRoyaleGames(long tournamentId) throws Exception {
        List<PlayerAvailability> availabilities = playerAvailabilityRepository.findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(tournamentId);
        List<Long> playerIds = availabilities.stream().map(PlayerAvailability::getPlayerId).toList();

        if (availabilities.isEmpty()) {
            throw new Exception("No player availabilities found for tournament %s".formatted(tournamentId));
        }

        List<Game> scheduledGames = new ArrayList<>();
        Map<OffsetDateTime, List<PlayerAvailability>> availabilityMap = groupAvailabilitiesByStartTime(availabilities);
        Map<Long, EloRank> playerEloRankMap = getPlayerEloRankMap(playerIds, tournamentId);

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

    // Helper method to check if two availability periods overlap
    private boolean isPlayerClanAvailabilityOverlapping(PlayerAvailability userAvailability, ClanAvailability clanAvailability) {
        return userAvailability.getStartTime().isBefore(clanAvailability.getEndTime()) &&
                userAvailability.getEndTime().isAfter(clanAvailability.getStartTime());
    }

    public Map<OffsetDateTime, List<PlayerAvailability>> groupAvailabilitiesByStartTime(List<PlayerAvailability> availabilities) {
        return availabilities.stream()
                .filter(PlayerAvailability::isAvailable)
                .collect(Collectors.groupingBy(PlayerAvailability::getStartTime));
    }

    public Map<OffsetDateTime, List<ClanAvailability>> groupClanAvailabilitiesByStartTime(List<ClanAvailability> availabilities) {
        return availabilities.stream()
                .filter(ClanAvailability::isAvailable)
                .collect(Collectors.groupingBy(ClanAvailability::getStartTime));
    }

    public Map<String, List<PlayerAvailability>> groupPlayersByRank(List<PlayerAvailability> availablePlayers, Map<Long, EloRank> eloRankMap) {
        Map<String, List<PlayerAvailability>> rankedPlayersMap = new HashMap<>();

        // Group players by their rank
        for (PlayerAvailability availability : availablePlayers) {
            Long playerId = availability.getPlayerId();
            EloRank playerEloRank = eloRankMap.get(playerId);
            if (playerEloRank == null) {
                continue;
            }
            String rank = String.valueOf(playerEloRank.getRankThresholdId()); // Get the player's rank

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
        if (playerIds == null || playerIds.isEmpty()) {
            return Collections.emptyMap();  // Return an empty map if no player IDs are provided
        }

        // Initialize the map to store player ID -> EloRank mapping
        Map<Long, EloRank> eloRankMap = new HashMap<>();

        // For each player ID, call the Feign client to get the player's EloRank and put it in the map
        for (Long playerId : playerIds) {
            try {
                // Get the Elo rank in the specified Map<String, Object> format
                Map<String, Object> res = eloRankingClient.getPlayerEloRank(playerId, tournamentId);
                PlayerEloRank eloRank = objectMapper.convertValue(res.get("data"), PlayerEloRank.class);

                // Add to the main eloRankMap with playerId as the key
                eloRankMap.put(playerId, eloRank);
            } catch (Exception e) {
                // Handle exception if the player EloRank could not be retrieved (optional logging or rethrow)
                // For now, we'll skip the player if an exception occurs.
                System.err.println("Error retrieving Elo rank for playerId: " + playerId + " in tournamentId: " + tournamentId + ". " + e.getMessage());
            }
        }

        return eloRankMap;
    }

    private List<TimeSlot> findOverlappingTimeSlots(List<ClanAvailability> clan1Availabilities, List<ClanAvailability> clan2Availabilities) {
        List<TimeSlot> overlappingSlots = new ArrayList<>();

        for (ClanAvailability ca1 : clan1Availabilities) {
            for (ClanAvailability ca2 : clan2Availabilities) {
                OffsetDateTime latestStart = ca1.getStartTime().isAfter(ca2.getStartTime()) ? ca1.getStartTime() : ca2.getStartTime();
                OffsetDateTime earliestEnd = ca1.getEndTime().isBefore(ca2.getEndTime()) ? ca1.getEndTime() : ca2.getEndTime();

                if (latestStart.isBefore(earliestEnd)) {
                    overlappingSlots.add(new TimeSlot(latestStart, earliestEnd));
                    System.out.printf("Found overlapping slot: %s to %s", latestStart, earliestEnd);
                }
            }
        }

        return overlappingSlots;
    }

    public static class TimeSlot {
        private OffsetDateTime startTime;
        private OffsetDateTime endTime;

        public TimeSlot(OffsetDateTime startTime, OffsetDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public OffsetDateTime getStartTime() {
            return startTime;
        }

        public OffsetDateTime getEndTime() {
            return endTime;
        }
    }
}
