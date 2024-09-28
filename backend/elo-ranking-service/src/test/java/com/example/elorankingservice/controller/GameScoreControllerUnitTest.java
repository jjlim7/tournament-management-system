package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.resources.ResultGenerator;
import com.example.elorankingservice.service.GameScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Map;

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
        Logger root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.isInfoEnabled();
        Logger serviceLogger = LoggerFactory.getLogger("com.example.elorankingservice");
        serviceLogger.isDebugEnabled();
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


        Request.CreateBattleRoyalePlayerGameScore req = new Request.CreateBattleRoyalePlayerGameScore();
        req.setRawPlayerGameScores(playerGameScores);
        // Test storing scores
        mockMvc.perform(post("/api/game-score/battle-royale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

    }

    @Test
    @Order(2)
    public void createAndRetrieveClanGameScore() throws Exception {
        long wClanId = 1L;
        long lClanId = 2L;
        String fileName = TEST_FILE_PATH + "clan_war_results_test.json";

        // Generate random clan war results
        ResultGenerator generator = new ResultGenerator();
        ResultGenerator.CreateClanWarGameScore result = generator.generateClanWarGameScores(TEST_GAME_ID, TEST_CW_TOURNAMENT_ID, wClanId, lClanId, fileName);


        // all players score
        List<PlayerGameScore> allPlayersScores = new ArrayList<>();
        Map<Long, List<PlayerGameScore>> winnerClan  = result.getWinnerRawPlayerGameScores();
        Map<Long, List<PlayerGameScore>> loserClan  = result.getLoserRawPlayerGameScores();

        for (Long clanId : winnerClan.keySet()) {
            allPlayersScores.addAll(winnerClan.get(clanId));
        }
        for (Long clanId : loserClan.keySet()) {
            allPlayersScores.addAll(loserClan.get(clanId));
        }

        System.out.println(winnerClan);
        System.out.println(loserClan);
        // Mock storing all player game scores for this game
        given(gameScoreService.storeAllPlayerGameScore(allPlayersScores)).willReturn(allPlayersScores);

        // create request body
        Request.CreateClanWarGameScore req = new Request.CreateClanWarGameScore();
        req.setWinnerRawPlayerGameScores(result.getWinnerRawPlayerGameScores().get(wClanId));
        req.setLoserRawPlayerGameScores(result.getLoserRawPlayerGameScores().get(lClanId));
        req.setGameId(TEST_GAME_ID);
        req.setTournamentId(TEST_CW_TOURNAMENT_ID);
        req.setLoserClanId(lClanId);
        req.setWinnerClanId(wClanId);


        // Test storing scores
        mockMvc.perform(post("/api/game-score/clan-war")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

    }
}