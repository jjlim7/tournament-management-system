//package com.example.elorankingservice.controller;
//
//import com.example.elorankingservice.dto.Request;
//import com.example.elorankingservice.entity.ClanEloRank;
//import com.example.elorankingservice.entity.PlayerEloRank;
//import com.example.elorankingservice.entity.PlayerGameScore;
//import com.example.elorankingservice.entity.RankThreshold;
//import com.example.elorankingservice.service.EloRankingService;
//import com.example.elorankingservice.service.RankService;
//import com.example.elorankingservice.resources.ResultGenerator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.test.web.servlet.MvcResult;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
//import java.util.List;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(EloRankingController.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource(properties = "logging.level.com.example.elorankingservice=DEBUG")
//public class EloRankingControllerUnitTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ResourceLoader resourceLoader;
//
//    @MockBean
//    private EloRankingService eloRankingService;
//    @MockBean
//    private RankService rankService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private static final String TEST_FILE_PATH = "src/test/java/com/example/elorankingservice/resources/";
//    private static final long TEST_GAME_ID = 1000L;
//    private static final long TEST_BR_TOURNAMENT_ID = 1001L;
//    private static final long TEST_CW_TOURNAMENT_ID = 1002L;
//
//
//    @BeforeEach
//    public void setUp() {
//        Logger root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        root.isInfoEnabled();
//        Logger serviceLogger = LoggerFactory.getLogger("com.example.elorankingservice");
//        serviceLogger.isDebugEnabled();
//        rankService.seedRankThresholds();
//        objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Test
//    @Order(1)
//    public void createEloRankingTest() throws Exception {
//        // ... (This test remains the same as it doesn't directly involve ResultGenerator)
//    }
//
//    @Test
//    @Order(2)
//    public void getEloRankingTest() throws Exception {
//        // ... (This test remains the same as it doesn't directly involve ResultGenerator)
//    }
//
//    @Test
//    @Order(3)
//    public void processBattleRoyaleResults() throws Exception {
//        int numberOfPlayers = 50;
//        String fileName = TEST_FILE_PATH + "battle_royale_results_test.json";
//
//        // Generate random battle royale results
//        ResultGenerator generator = new ResultGenerator();
//        generator.generateBattleRoyalePlayerGameScores(numberOfPlayers, TEST_GAME_ID, TEST_BR_TOURNAMENT_ID, fileName);
//
//        // Load battle royale result from json
//        Resource resource = resourceLoader.getResource("file:" + fileName);
//        List<PlayerGameScore> playerGameScores = objectMapper.readValue(resource.getInputStream(),
//                objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerGameScore.class));
//
//        // Create elo rank for players
//        for (PlayerGameScore playerGameScore : playerGameScores) {
//            Long playerId = playerGameScore.getPlayerId();
//            eloRankingService.createNewPlayerEloRanking(playerId, TEST_BR_TOURNAMENT_ID);
//        }
//
//        Request.CreateBattleRoyalePlayerGameScore req = new Request.CreateBattleRoyalePlayerGameScore();
//        req.setRawPlayerGameScores(playerGameScores);
//
//        // Perform the test
//        MvcResult mvcResult = mockMvc.perform(post("/api/elo-ranking/battle-royale")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andReturn();
//    }
//
//    @Test
//    @Order(4)
//    public void processClanWarResults() throws Exception {
//        long wClanId = 1L;
//        long lClanId = 2L;
//        String fileName = TEST_FILE_PATH + "clan_war_results_test.json";
//
//        // Generate random clan war results
//        ResultGenerator generator = new ResultGenerator();
//        ResultGenerator.CreateClanWarGameScore result = generator.generateClanWarGameScores(TEST_GAME_ID, TEST_CW_TOURNAMENT_ID, wClanId, lClanId, fileName);
//
//
//        for (Long clanId: result.getLoserRawPlayerGameScores().keySet()) {
//            eloRankingService.createNewClanEloRanking(clanId, TEST_CW_TOURNAMENT_ID);
//            for (PlayerGameScore playerGameScore : result.getLoserRawPlayerGameScores().get(clanId)) {
//                Long playerId = playerGameScore.getPlayerId();
//                eloRankingService.createNewPlayerEloRanking(playerId, TEST_CW_TOURNAMENT_ID);
//            }
//        }
//
//        for (Long clanId: result.getWinnerRawPlayerGameScores().keySet()) {
//            eloRankingService.createNewClanEloRanking(clanId, TEST_CW_TOURNAMENT_ID);
//            eloRankingService.createNewClanEloRanking(clanId, TEST_CW_TOURNAMENT_ID);
//            for (PlayerGameScore playerGameScore : result.getWinnerRawPlayerGameScores().get(clanId)) {
//                Long playerId = playerGameScore.getPlayerId();
//                eloRankingService.createNewPlayerEloRanking(playerId, TEST_CW_TOURNAMENT_ID);
//            }
//        }
//
//
//        // need to pass in variables
//        Request.CreateClanWarGameScore req = new Request.CreateClanWarGameScore();
//        req.setGameId(TEST_GAME_ID);
//        req.setLoserClanId(lClanId);
//        req.setWinnerClanId(wClanId);
//        req.setTournamentId(TEST_CW_TOURNAMENT_ID);
//        req.setLoserRawPlayerGameScores(result.getLoserRawPlayerGameScores().get(lClanId));
//        req.setWinnerRawPlayerGameScores(result.getWinnerRawPlayerGameScores().get(wClanId));
//
//
//        // Perform the test
//        MvcResult mvcResult = mockMvc.perform(post("/api/elo-ranking/clan-war")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andReturn();
//    }
//}