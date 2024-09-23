package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.entity.RankThreshold;
import com.example.elorankingservice.service.EloRankingService;
import com.example.elorankingservice.service.RankService;
import com.example.elorankingservice.resources.ResultGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




@WebMvcTest(EloRankingController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "logging.level.com.example.elorankingservice=DEBUG")
public class EloRankingControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private EloRankingService eloRankingService;
    @MockBean
    private RankService rankService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Logger root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.isInfoEnabled();
        Logger serviceLogger = LoggerFactory.getLogger("com.example.elorankingservice");
        serviceLogger.isDebugEnabled();
        rankService.seedRankThresholds();
    }

    @Test
    @Order(1)
    public void createEloRankingTest() throws Exception {
        Long mockClanId = new Random().nextLong();
        Long mockPlayerId = new Random().nextLong();
        Long mockTournamentId = new Random().nextLong();
        RankThreshold mockRankThreshold =  new RankThreshold(EloRankingService.ORIGIN_RANK, 0, 100);

        ClanEloRank mockClanEloRank = new ClanEloRank(mockClanId, mockRankThreshold, EloRankingService.INITIAL_MEAN, EloRankingService.INITIAL_SIGMA, mockTournamentId);

        PlayerEloRank mockPlayerEloRank = new PlayerEloRank(mockPlayerId, mockRankThreshold, EloRankingService.INITIAL_MEAN, EloRankingService.INITIAL_SIGMA, mockTournamentId);

        given(eloRankingService.createNewClanEloRanking(mockClanId, mockTournamentId)).willReturn(mockClanEloRank);
        given(eloRankingService.createNewPlayerEloRanking(mockPlayerId, mockTournamentId)).willReturn(mockPlayerEloRank);

        mockMvc.perform(post("/api/elo-ranking/clan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockClanEloRank)))
                .andExpect(status().isCreated())  // Check if the status is 200 OK
                .andExpect(jsonPath("$.clanId", is(mockClanId)))  // Validate the response body
                .andExpect(jsonPath("$.meanSkillEstimate", is(25.0)))  // Validate specific fields
                .andExpect(jsonPath("$.uncertainty", is(8.333)));

        mockMvc.perform(post("/api/elo-ranking/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPlayerEloRank)))
                .andExpect(status().isCreated())  // Check if the status is 200 OK
                .andExpect(jsonPath("$.playerId", is(mockPlayerId)))  // Validate the response body
                .andExpect(jsonPath("$.meanSkillEstimate", is(25.0)))  // Validate specific fields
                .andExpect(jsonPath("$.uncertainty", is(8.333)));

    }

    @Test
    @Order(2)
    public void getEloRankingTest() throws Exception {
        Long mockClanId = new Random().nextLong();
        Long mockPlayerId = new Random().nextLong();
        Long mockTournamentId = new Random().nextLong();
        RankThreshold mockRankThreshold = new RankThreshold(EloRankingService.ORIGIN_RANK, 0, 100);

        ClanEloRank mockClanEloRank = new ClanEloRank(mockClanId, mockRankThreshold, EloRankingService.INITIAL_MEAN, EloRankingService.INITIAL_SIGMA, mockTournamentId);
        PlayerEloRank mockPlayerEloRank = new PlayerEloRank(mockPlayerId, mockRankThreshold, EloRankingService.INITIAL_MEAN, EloRankingService.INITIAL_SIGMA, mockTournamentId);

        // Mock the service method for retrieving Elo rankings
        given(eloRankingService.retrieveClanEloRank(mockClanId, mockTournamentId)).willReturn(Optional.of(mockClanEloRank));
        given(eloRankingService.retrievePlayerEloRank(mockPlayerId, mockTournamentId)).willReturn(Optional.of(mockPlayerEloRank));

        // Test fetching clan Elo ranking
        mockMvc.perform(get("/api/elo-ranking/clan/{clanId}/tournament/{tournamentId}", mockClanId, mockTournamentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clanId", is(mockClanId)))
                .andExpect(jsonPath("$.meanSkillEstimate", is(25.0)))
                .andExpect(jsonPath("$.uncertainty", is(8.333)));

        // Test fetching player Elo ranking
        mockMvc.perform(get("/api/elo-ranking/player/{playerId}/tournament/{tournamentId}", mockPlayerId, mockTournamentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId", is(mockPlayerId)))
                .andExpect(jsonPath("$.meanSkillEstimate", is(25.0)))
                .andExpect(jsonPath("$.uncertainty", is(8.333)));
    }

    @Test
    @Order(3)
    public void processBattleRoyaleResults() throws Exception {
        // generate random battle royale results
        ResultGenerator generator = new ResultGenerator();
        generator.generateBattleRoyalePlayerGameScores(50, "src/test/java/com/example/elorankingservice/resources/battle_royale_results_test.json");

        // load battle royale result from json
        Resource resource = resourceLoader.getResource("file:src/test/java/com/example/elorankingservice/resources/battle_royale_results_test.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule to support Java 8 date/time types
        List<PlayerGameScore> playerGameScores = objectMapper.readValue(resource.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerGameScore.class));

        // create elo rank for 50 players, since the players from the battle royale has not been created
        for (PlayerGameScore playerGameScore : playerGameScores) {
            Long playerId = playerGameScore.getPlayerId();
            Long tournamentId = 1001L;
            eloRankingService.createNewPlayerEloRanking(playerId, tournamentId);
        }

        Request.CreateBattleRoyalePlayerGameScore req = new Request.CreateBattleRoyalePlayerGameScore();
        req.setRawPlayerGameScores(playerGameScores);

        // Capture the result of the mockMvc.perform
        MvcResult mvcResult = mockMvc.perform(post("/api/elo-ranking/battle-royale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))) // Send the player game scores as JSON
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }


    @Test
    public void processClanWarResults() throws Exception {

    }
}
