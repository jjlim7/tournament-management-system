package com.example.elorankingservice.controller;


import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.resources.ResultGenerator;
import com.example.elorankingservice.service.GameScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

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

    @Test
    public void createAndRetrievePlayerGameScore() throws Exception {
        // generate random battle royale results
        ResultGenerator generator = new ResultGenerator();
        generator.generateBattleRoyalePlayerGameScores(50, "src/test/java/com/example/elorankingservice/resources/battle_royale_results_test.json");

        // load battle royale result from json
        Resource resource = resourceLoader.getResource("file:src/test/java/com/example/elorankingservice/resources/battle_royale_results_test.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule to support Java 8 date/time types

        List<PlayerGameScore> playerGameScores = objectMapper.readValue(resource.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerGameScore.class));

        // take random entry
        PlayerGameScore testPlayerGameScore = playerGameScores.get(0);

        // create fake array
        List<PlayerGameScore> allPlayersScore = new ArrayList<>();
        allPlayersScore.add(testPlayerGameScore);

        // store all player game scores for this game
        gameScoreService.storeAllPlayerGameScore(playerGameScores);

        // retrieve test player game score
        given(gameScoreService.retrievePlayerGameScoresForTournament(testPlayerGameScore.getTournamentId(),testPlayerGameScore.getPlayerId())).willReturn(allPlayersScore);
    }

    @Test
    public void createAndRetrieveClanGameScore() throws Exception {

    }
}
