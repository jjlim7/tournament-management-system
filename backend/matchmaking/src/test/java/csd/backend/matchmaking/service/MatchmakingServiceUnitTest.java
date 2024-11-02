package csd.backend.matchmaking.service;

import csd.backend.matchmaking.dto.ClanEloRank;
import csd.backend.matchmaking.dto.EloRank;
import csd.backend.matchmaking.dto.PlayerEloRank;
import csd.backend.matchmaking.dto.RankThreshold;
import com.fasterxml.jackson.databind.ObjectMapper;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import csd.backend.matchmaking.services.GameService;
import csd.backend.matchmaking.services.MatchmakingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(MatchmakingService.class)
@AutoConfigureMockMvc(addFilters = false)
public class MatchmakingServiceUnitTest {

    @Autowired
    private MatchmakingService matchmakingService;

    @MockitoBean
    private EloRankingClient eloRankingClient;

    @MockitoBean
    private TournamentClient tournamentClient;

    @MockitoBean
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    @MockitoBean
    private ClanAvailabilityRepository clanAvailabilityRepository;

    @MockitoBean
    private GameRepository gameRepository;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private List<PlayerAvailability> mockPlayerAvailabilities;
    private List<PlayerEloRank> mockPlayerEloRanks;
    private Map<String, Object> mockEloRankingResponse;
    private Map<Long, EloRank> mockEloRankMap;
    private final long TOURNAMENT_ID = 1L;
    private final int MIN_PLAYERS_PER_GAME = 3;
    private final int MAX_PLAYERS_PER_GAME = 20;

    @BeforeEach
    public void setUp() {
        OffsetDateTime startTime = OffsetDateTime.parse(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).format(formatter));
        OffsetDateTime endTime = startTime.plusHours(2);

        // Initialize some mock player availabilities
        mockPlayerAvailabilities = Arrays.asList(
                new PlayerAvailability(1L, TOURNAMENT_ID, startTime, endTime, true),
                new PlayerAvailability(2L, TOURNAMENT_ID, startTime, endTime, true),
                new PlayerAvailability(3L, TOURNAMENT_ID, startTime, endTime, true)
        );

        mockPlayerEloRanks = Arrays.asList(
                new PlayerEloRank(1L, new RankThreshold(RankThreshold.Rank.GOLD, 1, 1L), 1L, 1L, 1L),
                new PlayerEloRank(2L, new RankThreshold(RankThreshold.Rank.GOLD, 1, 1L), 1L, 1L, 1L),
                new PlayerEloRank(3L, new RankThreshold(RankThreshold.Rank.GOLD, 1, 1L), 1L, 1L, 1L)
        );

        mockEloRankingResponse = new HashMap<>();
        mockEloRankingResponse.put("playerEloRanks", mockPlayerEloRanks);

        // Mock EloRank data
        mockEloRankMap = mockPlayerEloRanks.stream()
                .collect(Collectors.toMap(
                        PlayerEloRank::getPlayerId,   // Use playerId as the key
                        playerEloRank -> playerEloRank
                ));
    }

    @Test
    void scheduleBattleRoyaleGames_noPlayerAvailabilities_shouldThrowException() {
        // Arrange: Mock empty player availabilities
        when(playerAvailabilityRepository.findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(TOURNAMENT_ID))
                .thenReturn(Collections.emptyList());

        // Act & Assert: Expect an exception due to no available players
        Exception exception = assertThrows(Exception.class, () -> {
            matchmakingService.scheduleBattleRoyaleGames(TOURNAMENT_ID);
        });

        assertThat(exception.getMessage()).contains("No player availabilities found for tournament " + TOURNAMENT_ID);
    }

    @Test
    void scheduleBattleRoyaleGames_successfulScheduling() throws Exception {
        long tournamentId = 1L;

        // Mock Player Availability
        PlayerAvailability pa1 = new PlayerAvailability();
        pa1.setPlayerId(100L);
        pa1.setStartTime(OffsetDateTime.parse("2024-06-01T10:00:00Z"));
        pa1.setEndTime(OffsetDateTime.parse("2024-06-01T12:00:00Z"));
        pa1.setAvailable(true);

        PlayerAvailability pa2 = new PlayerAvailability();
        pa2.setPlayerId(101L);
        pa2.setStartTime(OffsetDateTime.parse("2024-06-01T10:00:00Z"));
        pa2.setEndTime(OffsetDateTime.parse("2024-06-01T12:00:00Z"));
        pa2.setAvailable(true);

        PlayerAvailability pa3 = new PlayerAvailability();
        pa3.setPlayerId(102L);
        pa3.setStartTime(OffsetDateTime.parse("2024-06-01T10:00:00Z"));
        pa3.setEndTime(OffsetDateTime.parse("2024-06-01T12:00:00Z"));
        pa3.setAvailable(true);

        List<PlayerAvailability> playerAvailabilities = List.of(pa1, pa2, pa3);
        when(playerAvailabilityRepository.findAllByTournamentIdAndIsAvailableTrueOrderByStartTime(tournamentId))
                .thenReturn(playerAvailabilities);

        // Mock Elo Rank Map
        PlayerEloRank rank1 = new PlayerEloRank(100L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 0), 1200, 100, tournamentId);
        PlayerEloRank rank2 = new PlayerEloRank(101L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 0), 1400, 100, tournamentId);
        PlayerEloRank rank3 = new PlayerEloRank(102L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 0), 1000, 100, tournamentId);

        // Mock the Feign Client to return Elo ranks for each player
        when(eloRankingClient.getPlayerEloRank(100L, tournamentId))
                .thenReturn(rank1);
        when(eloRankingClient.getPlayerEloRank(101L, tournamentId))
                .thenReturn(rank2);
        when(eloRankingClient.getPlayerEloRank(102L, tournamentId))
                .thenReturn(rank3);

        // Mock GameService to create games
        Game game = new Game();
        game.setGameId(500L);
        when(gameService.createBattleRoyaleGame(
                eq(tournamentId),
                anyList(),
                any(),
                any(),
                eq(Game.GameStatus.SCHEDULED)
        )).thenReturn(game);

        // Execute the matchmaking logic
        List<Game> scheduledGames = matchmakingService.scheduleBattleRoyaleGames(tournamentId);

        // Verify the outcome
        assertEquals(1, scheduledGames.size());  // Only one game should be scheduled
        assertEquals(500L, scheduledGames.get(0).getGameId());

        // Verify that the Elo ranks were fetched using the FeignClient
        verify(eloRankingClient, times(1)).getPlayerEloRank(100L, tournamentId);
        verify(eloRankingClient, times(1)).getPlayerEloRank(101L, tournamentId);
        verify(eloRankingClient, times(1)).getPlayerEloRank(102L, tournamentId);

        // Capture the player IDs used in game creation
        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(gameService, times(1)).createBattleRoyaleGame(
                eq(tournamentId),
                captor.capture(),
                any(),
                any(),
                eq(Game.GameStatus.SCHEDULED)
        );

        // Verify that all players were added to the game
        List<Long> capturedPlayerIds = captor.getValue();
        assertEquals(List.of(100L, 101L, 102L), capturedPlayerIds);
    }

    @Test
    void scheduleClanWarGames_successfulScheduling() throws Exception {
        long tournamentId = 1L;

        // Mock ClanAvailability
        ClanAvailability ca1 = new ClanAvailability();
        ca1.setClanId(100L);
        ca1.setStartTime(OffsetDateTime.parse("2024-05-01T10:00:00Z"));
        ca1.setEndTime(OffsetDateTime.parse("2024-05-01T12:00:00Z"));

        ClanAvailability ca2 = new ClanAvailability();
        ca2.setClanId(101L);
        ca2.setStartTime(OffsetDateTime.parse("2024-05-01T11:00:00Z"));
        ca2.setEndTime(OffsetDateTime.parse("2024-05-01T13:00:00Z"));

        List<ClanAvailability> clanAvailabilities = List.of(ca1, ca2);
        when(clanAvailabilityRepository.findClanAvailabilitiesByTournamentId(tournamentId)).thenReturn(clanAvailabilities);

        // Mock Feign Client to return ELO ranks for each clan
        ClanEloRank clanEloRank1 = new ClanEloRank(100L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 0), 1500.0, 100.0, tournamentId);
        ClanEloRank clanEloRank2 = new ClanEloRank(101L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 0), 1450.0, 100.0, tournamentId);
        when(eloRankingClient.getClanEloRank(100L, tournamentId)).thenReturn(clanEloRank1);
        when(eloRankingClient.getClanEloRank(101L, tournamentId)).thenReturn(clanEloRank2);


        // Mock GameService
        Game game = new Game();
        game.setGameId(500L);
        when(gameService.createClanWarGame(
                eq(tournamentId),
                anyList(),
                any(),
                any(),
                eq(Game.GameStatus.SCHEDULED)
        )).thenReturn(game);

        // Execute
        List<Game> scheduledGames = matchmakingService.scheduleClanWarGames(tournamentId);

        // Verify
        assertEquals(1, scheduledGames.size());
        assertEquals(500L, scheduledGames.get(0).getGameId());

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(gameService, times(1)).createClanWarGame(
                eq(tournamentId),
                captor.capture(),
                eq(OffsetDateTime.parse("2024-05-01T11:00:00Z")),
                eq(OffsetDateTime.parse("2024-05-01T12:00:00Z")),
                eq(Game.GameStatus.SCHEDULED)
        );

        // Create a mutable list from the captured value
        List<Long> capturedClans = new ArrayList<>(captor.getValue());
        Collections.sort(capturedClans);  // Sort the list before asserting
        assertEquals(List.of(100L, 101L), capturedClans);  // Now it should match
    }

    @Test
    void scheduleClanWarGames_noOverlappingAvailability() throws Exception {
        long tournamentId = 2L;

        // Mock ClanAvailability with no overlap
        ClanAvailability ca1 = new ClanAvailability();
        ca1.setClanId(200L);
        ca1.setStartTime(OffsetDateTime.parse("2024-06-01T10:00:00Z"));
        ca1.setEndTime(OffsetDateTime.parse("2024-06-01T12:00:00Z"));

        ClanAvailability ca2 = new ClanAvailability();
        ca2.setClanId(201L);
        ca2.setStartTime(OffsetDateTime.parse("2024-06-01T13:00:00Z"));
        ca2.setEndTime(OffsetDateTime.parse("2024-06-01T15:00:00Z"));

        List<ClanAvailability> clanAvailabilities = List.of(ca1, ca2);
        when(clanAvailabilityRepository.findClanAvailabilitiesByTournamentId(tournamentId)).thenReturn(clanAvailabilities);

        // Mock Feign Client to return ELO ranks for each clan
        ClanEloRank clanEloRank1 = new ClanEloRank(200L, new RankThreshold(RankThreshold.Rank.SILVER, 0, 0), 1500.0, 100.0, tournamentId);
        ClanEloRank clanEloRank2 = new ClanEloRank(201L, new RankThreshold(RankThreshold.Rank.SILVER, 0, 0), 1450.0, 100.0, tournamentId);
        when(eloRankingClient.getClanEloRank(200L, tournamentId)).thenReturn(clanEloRank1);
        when(eloRankingClient.getClanEloRank(201L, tournamentId)).thenReturn(clanEloRank2);

        // Execute the matchmaking logic
        List<Game> scheduledGames = matchmakingService.scheduleClanWarGames(tournamentId);

        // Verify that no games were scheduled due to lack of overlapping availability
        assertEquals(0, scheduledGames.size());

        // Verify that the ELO ranks were fetched using the FeignClient
        verify(eloRankingClient, times(1)).getClanEloRank(200L, tournamentId);
        verify(eloRankingClient, times(1)).getClanEloRank(201L, tournamentId);

        // Verify that no games were created
        verify(gameService, times(0)).createClanWarGame(anyLong(), anyList(), any(), any(), any());
    }

    @Test
    void scheduleClanWarGames_oddNumberOfClans() throws Exception {
        long tournamentId = 3L;

        // Mock ClanAvailability for three clans
        ClanAvailability ca1 = new ClanAvailability();
        ca1.setClanId(300L);
        ca1.setStartTime(OffsetDateTime.parse("2024-07-01T10:00:00Z"));
        ca1.setEndTime(OffsetDateTime.parse("2024-07-01T12:00:00Z"));

        ClanAvailability ca2 = new ClanAvailability();
        ca2.setClanId(301L);
        ca2.setStartTime(OffsetDateTime.parse("2024-07-01T10:30:00Z"));
        ca2.setEndTime(OffsetDateTime.parse("2024-07-01T12:30:00Z"));

        ClanAvailability ca3 = new ClanAvailability();
        ca3.setClanId(302L);
        ca3.setStartTime(OffsetDateTime.parse("2024-07-01T11:00:00Z"));
        ca3.setEndTime(OffsetDateTime.parse("2024-07-01T13:00:00Z"));

        List<ClanAvailability> clanAvailabilities = List.of(ca1, ca2, ca3);
        when(clanAvailabilityRepository.findClanAvailabilitiesByTournamentId(tournamentId)).thenReturn(clanAvailabilities);

        // Mock Feign Client to return ELO ranks
        ClanEloRank clanEloRank1 = new ClanEloRank(300L, new RankThreshold(RankThreshold.Rank.PLATINUM, 0, 0), 1700.0, 100.0, tournamentId);
        ClanEloRank clanEloRank2 = new ClanEloRank(301L, new RankThreshold(RankThreshold.Rank.PLATINUM, 0, 0), 1650.0, 100.0, tournamentId);
        ClanEloRank clanEloRank3 = new ClanEloRank(302L, new RankThreshold(RankThreshold.Rank.PLATINUM, 0, 0), 1600.0, 100.0, tournamentId);
        when(eloRankingClient.getClanEloRank(300L, tournamentId)).thenReturn(clanEloRank1);
        when(eloRankingClient.getClanEloRank(301L, tournamentId)).thenReturn(clanEloRank2);
        when(eloRankingClient.getClanEloRank(302L, tournamentId)).thenReturn(clanEloRank3);

        // Mock GameService to create games
        Game game = new Game();
        game.setGameId(600L);
        when(gameService.createClanWarGame(
                eq(tournamentId),
                anyList(),  // Avoid strict matching on list order
                any(),
                any(),
                eq(Game.GameStatus.SCHEDULED)
        )).thenReturn(game);

        // Execute the matchmaking logic
        List<Game> scheduledGames = matchmakingService.scheduleClanWarGames(tournamentId);

        // Verify the outcome
        assertEquals(1, scheduledGames.size());  // Only one game should be scheduled
        assertEquals(600L, scheduledGames.get(0).getGameId());

        // Capture the clan IDs used to create the game
        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(gameService, times(1)).createClanWarGame(
                eq(tournamentId),
                captor.capture(),  // Capture the list of clans used in game creation
                any(),
                any(),
                eq(Game.GameStatus.SCHEDULED)
        );

        // Sort and verify the captured clans
        List<Long> capturedClans = new ArrayList<>(captor.getValue());
        Collections.sort(capturedClans);

        List<Long> validPair1 = List.of(300L, 301L);
        List<Long> validPair2 = List.of(300L, 302L);
        List<Long> validPair3 = List.of(301L, 302L);

        // Ensure the captured list matches any of the valid pairs
        assertTrue(capturedClans.equals(validPair1) || capturedClans.equals(validPair2) || capturedClans.equals(validPair3));

        // Verify that the third clan (302) was not paired and no game was created for it
        verify(gameService, times(1)).createClanWarGame(eq(tournamentId), anyList(), any(), any(), any());
    }

}
