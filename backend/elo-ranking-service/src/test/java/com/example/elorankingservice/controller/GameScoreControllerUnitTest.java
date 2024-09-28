package com.example.elorankingservice.controller;

import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.resources.ResultGenerator;
import com.example.elorankingservice.service.GameScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(GameScoreController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "logging.level.com.example.elorankingservice=DEBUG")
public class GameScoreControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private GameScoreService gameScoreService;

    private ObjectMapper objectMapper;

    private static final String TEST_FILE_PATH = "src/test/java/com/example/elorankingservice/resources/";
    private static final long TEST_GAME_ID = 1000L;
    private static final long TEST_BR_TOURNAMENT_ID = 1001L;
    private static final long TEST_CW_TOURNAMENT_ID = 1001L;


    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Order(1)
    public void createAndRetrievePlayerGameScore() throws Exception {
        int numberOfPlayers = 50;
        String fileName = TEST_FILE_PATH + "battle_royale_results_test.json";

        // Generate random battle royale results
        ResultGenerator generator = new ResultGenerator();
        generator.generateBattleRoyalePlayerGameScores(numberOfPlayers, TEST_GAME_ID, TEST_BR_TOURNAMENT_ID, fileName);

        // Load battle royale result from json
        Resource resource = resourceLoader.getResource("file:" + fileName);
        List<PlayerGameScore> playerGameScores = objectMapper.readValue(resource.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerGameScore.class));

        // Take random entry
        PlayerGameScore testPlayerGameScore = playerGameScores.get(0);

        // Create fake array
        List<PlayerGameScore> allPlayersScore = new ArrayList<>();
        allPlayersScore.add(testPlayerGameScore);

        // Mock storing all player game scores for this game
        given(gameScoreService.storeAllPlayerGameScore(playerGameScores)).willReturn(playerGameScores);

        // Mock retrieving test player game score
        given(gameScoreService.retrievePlayerGameScoresForTournament(TEST_BR_TOURNAMENT_ID, testPlayerGameScore.getPlayerId()))
                .willReturn(allPlayersScore);

        // Test storing scores
        mockMvc.perform(post("/api/game-scores/battle-royale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerGameScores)))
                .andExpect(status().isCreated());

        // Test retrieving scores
        mockMvc.perform(get("/api/game-scores/player/{playerId}/tournament/{tournamentId}",
                        testPlayerGameScore.getPlayerId(), TEST_BR_TOURNAMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].playerId").value(testPlayerGameScore.getPlayerId()))
                .andExpect(jsonPath("$[0].tournamentId").value(TEST_BR_TOURNAMENT_ID));
    }

    @Test
    @Order(2)
    public void createAndRetrieveClanGameScore() throws Exception {
        long wClanId = 1L;
        long lClanId = 2L;
        String fileName = TEST_FILE_PATH + "clan_war_results_test.json";

        // Generate random clan war results
        ResultGenerator generator = new ResultGenerator();
        generator.generateClanWarGameScores(TEST_GAME_ID, TEST_CW_TOURNAMENT_ID, wClanId, lClanId, fileName);

        // Load clan war result from json
        Resource resource = resourceLoader.getResource("file:" + fileName);
        List<PlayerGameScore> playerGameScores = objectMapper.readValue(resource.getInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerGameScore.class));

        // Mock storing all player game scores for this game
        given(gameScoreService.storeAllPlayerGameScore(playerGameScores)).willReturn(playerGameScores);

        // Mock retrieving game scores for each clan
        given(gameScoreService.retrievePlayerGameScoresForTournament(TEST_CW_TOURNAMENT_ID, wClanId))
                .willReturn(playerGameScores.subList(0, 5));
        given(gameScoreService.retrievePlayerGameScoresForTournament(TEST_CW_TOURNAMENT_ID, lClanId))
                .willReturn(playerGameScores.subList(5, 10));

        // Test storing scores
        mockMvc.perform(post("/api/game-scores/clan-war")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerGameScores)))
                .andExpect(status().isCreated());

        // Test retrieving scores for winning clan
        mockMvc.perform(get("/api/game-scores/clan/{clanId}/tournament/{tournamentId}",
                        wClanId, TEST_CW_TOURNAMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].tournamentId").value(TEST_CW_TOURNAMENT_ID));

        // Test retrieving scores for losing clan
        mockMvc.perform(get("/api/game-scores/clan/{clanId}/tournament/{tournamentId}",
                        lClanId, TEST_CW_TOURNAMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].tournamentId").value(TEST_CW_TOURNAMENT_ID));
    }
}