package csd.backend.matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.PlayerAvailabilityService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerAvailabilityController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlayerAvailabilityControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerAvailabilityService playerAvailabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Order(1)
    public void testCreateAvailability() throws Exception {
        long playerId = 1L;
        long tournamentId = 1L;
        OffsetDateTime startTime = OffsetDateTime.now();
        OffsetDateTime endTime = OffsetDateTime.now();
        boolean isAvailable = true;

        String expectedStartTime = startTime.format(formatter);
        String expectedEndTime = endTime.format(formatter);

        // Set properties for mockAvailability if necessary
        PlayerAvailability mockAvailability = new PlayerAvailability(
                playerId,
                tournamentId,
                startTime,
                endTime,
                isAvailable
        );

        given(playerAvailabilityService.createAvailability(any(PlayerAvailability.class)))
                .willReturn(mockAvailability);

        mockMvc.perform(post("/api/playersAvailability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAvailability)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerId").value(1L))
                .andExpect(jsonPath("$.tournamentId").value(1L))
                .andExpect(jsonPath("$.startTime").value(expectedStartTime))
                .andExpect(jsonPath("$.endTime").value(expectedEndTime));

    }

    @Test
    public void testFindPlayerAvailabilityByPlayerId() throws Exception {
        long playerId = 1L;
        long tournamentId = 1L;
        boolean isAvailable = true;
        List<PlayerAvailability> playerAvailabilityList = new ArrayList<>();
        playerAvailabilityList.add(new PlayerAvailability(playerId, tournamentId, OffsetDateTime.now(), OffsetDateTime.now(), isAvailable));

        given(playerAvailabilityService.getPlayerAvailabilitiesByPlayerId(playerId))
                .willReturn(playerAvailabilityList);

        mockMvc.perform(get("/api/playersAvailability?playerId={playerId}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerId").value(playerId));
    }

    @Test
    public void testFindPlayerAvailabilityByTournamentId() throws Exception {
        long playerId = 1L;
        long tournamentId = 1L;
        boolean isAvailable = true;
        List<PlayerAvailability> playerAvailabilityList = new ArrayList<>();
        playerAvailabilityList.add(new PlayerAvailability(playerId, tournamentId, OffsetDateTime.now(), OffsetDateTime.now(), isAvailable));

        given(playerAvailabilityService.getPlayerAvailabilitiesByTournamentId(tournamentId))
                .willReturn(playerAvailabilityList);

        mockMvc.perform(get("/api/playersAvailability?tournamentId={tournamentId}", tournamentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tournamentId").value(tournamentId));
    }
}

