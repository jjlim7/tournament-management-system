package csd.backend.matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import csd.backend.matchmaking.dto.Response;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private static final long TOURNAMENT_ID = 1L;
    private static final List<Long> PLAYER_IDS = Arrays.asList(1L, 2L, 3L);
    private static final Game.GameMode GAME_MODE = Game.GameMode.BATTLE_ROYALE;
    private static final Game.GameStatus INITIAL_STATUS = Game.GameStatus.SCHEDULED;

    private Game game;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        game = createTestGame();
    }

    private Game createTestGame() {
        OffsetDateTime startTime = OffsetDateTime.parse(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).format(formatter));
        return new Game(TOURNAMENT_ID, PLAYER_IDS, null, startTime, startTime.plusHours(2), GAME_MODE, INITIAL_STATUS);
    }

    @Test
    void createBattleRoyaleGame() throws Exception {
        given(gameService.createGame(Game.GameMode.BATTLE_ROYALE, TOURNAMENT_ID, PLAYER_IDS, null, game.getStartTime(), game.getEndTime(), INITIAL_STATUS)).willReturn(game);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId").value(game.getGameId()))
                .andExpect(jsonPath("$.tournamentId").value(TOURNAMENT_ID))
                .andExpect(jsonPath("$.playerIds").isArray());
    }

    @Test
    void testUpdateGameStatus() throws Exception {
        long gameId = 1L;
        Game.GameStatus newStatus = Game.GameStatus.IN_PROGRESS;

        given(gameService.getGameById(gameId)).willReturn(game);

        game.setGameStatus(newStatus);
        given(gameService.updateGameStatus(gameId, newStatus)).willReturn(game);

        mockMvc.perform(put("/api/games/{gameId}/status", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus").value(newStatus.name()));
    }

    @Test
    void getGamesByTournament() throws Exception {
        List<Game> gameList = Collections.singletonList(game);
        given(gameService.getGamesByTournament(TOURNAMENT_ID)).willReturn(gameList);

        mockMvc.perform(get("/api/games?tournamentId={tournamentId}", TOURNAMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tournamentId").value(TOURNAMENT_ID))
                .andExpect(jsonPath("$[0].gameStatus").value(INITIAL_STATUS.name()))
                .andExpect(jsonPath("$[0].playerIds").isArray());
    }

    @Test
    void getPlayerIdsByGame() throws Exception {
        given(gameService.getPlayerIdsByGame(game.getGameId())).willReturn(PLAYER_IDS);

        mockMvc.perform(get("/api/games/{gameId}/playerIds", game.getGameId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PLAYER_IDS)));
    }

    @Test
    void getPlayerOrClanIdsByGame() throws Exception {
        // Mock for PlayerIDs
        Response.EntityIdResponse response = new Response.EntityIdResponse("PLAYER", PLAYER_IDS);

        given(gameService.getGamePlayerOrClanIds(game.getGameId())).willReturn(response);

        mockMvc.perform(get("/api/games/{gameId}/playerOrClanIds", game.getGameId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
