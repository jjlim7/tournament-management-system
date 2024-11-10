package csd.backend.matchmaking.integration;

import csd.backend.matchmaking.feigndto.ClanEloRank;
import csd.backend.matchmaking.feigndto.PlayerEloRank;
import csd.backend.matchmaking.feigndto.RankThreshold;
import csd.backend.matchmaking.feigndto.Tournament;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback after each test to avoid side effects
class MatchmakingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    @Autowired
    private ClanAvailabilityRepository clanAvailabilityRepository;

    @Autowired
    private GameRepository gameRepository;

    @MockitoBean
    private TournamentClient tournamentClient;

    @MockitoBean
    private EloRankingClient eloRankingClient;

    @BeforeEach
    void setup() {
        // Clean up the database before each test
        playerAvailabilityRepository.deleteAll();
        clanAvailabilityRepository.deleteAll();
        gameRepository.deleteAll();

        // Mock the TournamentClient response
        Tournament tournament = new Tournament();
        tournament.setTournament_id(200L);
        tournament.setName("BATTLE ROYALE Championship");
        tournament.setDescription("A tournament for top players in Spring season.");
        tournament.setStartDate(OffsetDateTime.now().plusDays(1));  // Start in 1 day
        tournament.setEndDate(OffsetDateTime.now().plusDays(10));   // Ends in 10 days
        tournament.setPlayerCapacity(100);
        tournament.setStatus(Tournament.Status.ACTIVE);
        tournament.setGameMode(Tournament.GameMode.BATTLE_ROYALE);
        tournament.setAdminId(123L);  // Example admin ID
        tournament.setPlayerIds(List.of(1L, 2L, 3L, 4L));

        // Mock the TournamentClient response
        Tournament tournament2 = new Tournament();
        tournament2.setTournament_id(400L);
        tournament2.setName("CLAN WAR CHAMPIONSHIP");
        tournament2.setDescription("A tournament for top players in Spring season.");
        tournament2.setStartDate(OffsetDateTime.now().plusDays(1));  // Start in 1 day
        tournament2.setEndDate(OffsetDateTime.now().plusDays(10));   // Ends in 10 days
        tournament2.setPlayerCapacity(100);
        tournament2.setStatus(Tournament.Status.ACTIVE);
        tournament2.setGameMode(Tournament.GameMode.CLANWAR);
        tournament2.setAdminId(123L);  // Example admin ID
        tournament2.setPlayerIds(List.of(1L, 2L, 3L, 4L));

        // Create a mock response for TournamentClient
        when(tournamentClient.getTournamentById(eq(200L))).thenReturn(new ResponseEntity<>(tournament, HttpStatus.OK));  // Mock the tournament with ID 400
        when(tournamentClient.getTournamentById(eq(400L))).thenReturn(new ResponseEntity<>(tournament2, HttpStatus.OK));  // Mock the tournament with ID 400
    }

    @Test
    void scheduleBattleRoyaleGames_success() throws Exception {
        /** MINIMUM PLAYER IS 3
         * */

        // Mock the EloRankingClient response for players in BattleRoyale
        PlayerEloRank playerEloRank1 = new PlayerEloRank(100L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 1500), 1450.0, 100.0, 200L);
        Map<String, Object> playerEloRankMap1 = Map.of(
                "status", "success",
                "data", playerEloRank1
        );
        when(eloRankingClient.getPlayerEloRank(eq(100L), eq(200L))).thenReturn(playerEloRankMap1);

        PlayerEloRank playerEloRank2 = new PlayerEloRank(101L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 1500), 1250.0, 200.0, 200L);
        Map<String, Object> playerEloRankMap2 = Map.of(
                "status", "success",
                "data", playerEloRank2
        );
        when(eloRankingClient.getPlayerEloRank(eq(101L), eq(200L))).thenReturn(playerEloRankMap2);

        PlayerEloRank playerEloRank3 = new PlayerEloRank(102L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 1500), 1250.0, 200.0, 200L);
        Map<String, Object> playerEloRankMap3 = Map.of(
                "status", "success",
                "data", playerEloRank3
        );
        when(eloRankingClient.getPlayerEloRank(eq(102L), eq(200L))).thenReturn(playerEloRankMap3);

        // Insert test data for Player Availability
        PlayerAvailability player1 = new PlayerAvailability(
                null, 100L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        PlayerAvailability player2 = new PlayerAvailability(
                null, 101L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        PlayerAvailability player3 = new PlayerAvailability(
                null, 102L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        playerAvailabilityRepository.saveAll(List.of(player1, player2, player3));

        // Perform POST request to schedule games and verify the response
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", "200")
                        .param("gameMode", "BATTLE_ROYALE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Games scheduled for tournament 200"));

        // Verify that the games were created in the database
        assert gameRepository.findAll().size() == 1;
    }

    @Test
    void scheduleClanWarGames_success() throws Exception {
        // Mock the EloRankingClient response for clans
        ClanEloRank clanEloRank = new ClanEloRank(300L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 1500), 1400.0, 200.0, 400L);
        Map<String, Object> clanEloRankMap = Map.of(
                "status", "success",
                "data", clanEloRank
        );

        when(eloRankingClient.getClanEloRank(eq(300L), eq(400L))).thenReturn(clanEloRankMap);

        ClanEloRank clanEloRank2 = new ClanEloRank(301L, new RankThreshold(RankThreshold.Rank.GOLD, 0, 1500), 1100.0, 300.0, 400L);
        Map<String, Object> clanEloRank2Map = Map.of(
                "status", "success",
                "data", clanEloRank2
        );
        when(eloRankingClient.getClanEloRank(eq(301L), eq(400L))).thenReturn(clanEloRank2Map);

        // Insert test data for Clan Availability
        ClanAvailability clan1 = new ClanAvailability(
                null, 300L, 400L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        ClanAvailability clan2 = new ClanAvailability(
                null, 301L, 400L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        clanAvailabilityRepository.saveAll(List.of(clan1, clan2));

        // Perform POST request to schedule games and verify the response
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", "400")
                        .param("gameMode", "CLAN_WAR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Games scheduled for tournament 400"));

        // Verify that the games were created in the database
        assert gameRepository.findAll().size() == 1;
    }
}
