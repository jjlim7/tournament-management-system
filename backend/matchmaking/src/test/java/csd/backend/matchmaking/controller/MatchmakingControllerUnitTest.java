package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Tournament;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.services.MatchmakingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchmakingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MatchmakingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MatchmakingService matchmakingService;

    @MockitoBean
    private TournamentClient tournamentClient;

    @MockitoBean
    private EloRankingClient eloRankingClient;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final long TOURNAMENT_ID = 1L;
    private final String GAME_MODE = "BATTLE_ROYALE";

    @Test
    void scheduleGames_tournamentAlreadyScheduled_shouldReturnConflict() throws Exception {
        // Arrange
        given(matchmakingService.isTournamentAlreadyScheduled(TOURNAMENT_ID)).willReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", String.valueOf(TOURNAMENT_ID))
                        .param("gameMode", GAME_MODE))
                .andExpect(status().isConflict())
                .andExpect(content().string("The tournament has already been scheduled."));

        verify(matchmakingService, times(1)).isTournamentAlreadyScheduled(TOURNAMENT_ID);
    }

    @Test
    void scheduleGames_tournamentNotFound_shouldReturnBadRequest() throws Exception {
        // Arrange
        when(matchmakingService.isTournamentAlreadyScheduled(TOURNAMENT_ID)).thenReturn(false);
        when(tournamentClient.getTournamentById(TOURNAMENT_ID))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", String.valueOf(TOURNAMENT_ID))
                        .param("gameMode", GAME_MODE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tournament ID " + TOURNAMENT_ID + " not found."));

        verify(matchmakingService, times(1)).isTournamentAlreadyScheduled(TOURNAMENT_ID);
        verify(tournamentClient, times(1)).getTournamentById(TOURNAMENT_ID);
    }

    @Test
    void scheduleGames_successfulGameScheduling_shouldReturnCreated() throws Exception {
        // Arrange
        when(matchmakingService.isTournamentAlreadyScheduled(TOURNAMENT_ID)).thenReturn(false);
        when(tournamentClient.getTournamentById(TOURNAMENT_ID))
                .thenReturn(new ResponseEntity<>(new Tournament(), HttpStatus.OK));
        when(matchmakingService.scheduleGames(TOURNAMENT_ID, GAME_MODE))
                .thenReturn(List.of(new Game()));

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", String.valueOf(TOURNAMENT_ID))
                        .param("gameMode", GAME_MODE))
                .andExpect(status().isOk())
                .andExpect(content().string("Games scheduled for tournament " + TOURNAMENT_ID));

        verify(matchmakingService, times(1)).isTournamentAlreadyScheduled(TOURNAMENT_ID);
        verify(tournamentClient, times(1)).getTournamentById(TOURNAMENT_ID);
        verify(matchmakingService, times(1)).scheduleGames(TOURNAMENT_ID, GAME_MODE);
    }

    @Test
    void scheduleGames_failedToScheduleGames_shouldReturnBadRequest() throws Exception {
        // Arrange
        when(matchmakingService.isTournamentAlreadyScheduled(TOURNAMENT_ID)).thenReturn(false);
        when(tournamentClient.getTournamentById(TOURNAMENT_ID))
                .thenReturn(new ResponseEntity<>(new Tournament(), HttpStatus.OK));
        when(matchmakingService.scheduleGames(TOURNAMENT_ID, GAME_MODE))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", String.valueOf(TOURNAMENT_ID))
                        .param("gameMode", GAME_MODE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to schedule games for tournament " + TOURNAMENT_ID));

        verify(matchmakingService, times(1)).isTournamentAlreadyScheduled(TOURNAMENT_ID);
        verify(tournamentClient, times(1)).getTournamentById(TOURNAMENT_ID);
        verify(matchmakingService, times(1)).scheduleGames(TOURNAMENT_ID, GAME_MODE);
    }

    @Test
    void scheduleGames_internalServerError_shouldReturnInternalServerError() throws Exception {
        // Arrange
        when(matchmakingService.isTournamentAlreadyScheduled(TOURNAMENT_ID)).thenReturn(false);
        when(tournamentClient.getTournamentById(TOURNAMENT_ID))
                .thenReturn(new ResponseEntity<>(new Tournament(), HttpStatus.OK));
        when(matchmakingService.scheduleGames(TOURNAMENT_ID, GAME_MODE)).thenThrow(new Exception("Scheduling failed"));

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/scheduleGames")
                        .param("tournamentId", String.valueOf(TOURNAMENT_ID))
                        .param("gameMode", GAME_MODE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error. Failed to schedule games for tournament " + TOURNAMENT_ID));

        verify(matchmakingService, times(1)).isTournamentAlreadyScheduled(TOURNAMENT_ID);
        verify(tournamentClient, times(1)).getTournamentById(TOURNAMENT_ID);
        verify(matchmakingService, times(1)).scheduleGames(TOURNAMENT_ID, GAME_MODE);
    }
}