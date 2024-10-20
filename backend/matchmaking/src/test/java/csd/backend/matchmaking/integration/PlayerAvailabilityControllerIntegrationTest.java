package csd.backend.matchmaking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import csd.backend.matchmaking.controller.PlayerAvailabilityController;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import csd.backend.matchmaking.services.PlayerAvailabilityService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback after each test to avoid side effects
public class PlayerAvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    // You can use @BeforeEach to seed the database or rely on an import.sql file
    @BeforeEach
    public void setup() {
        // Optionally clear and seed the database here if necessary
        //playerAvailabilityRepository.deleteAll();

        // Create and save test data here, or rely on external seeding methods
        // Seed test data
    }

    @Test
    public void testGetPlayerAvailabilityByPlayerId() throws Exception {
        // Assume you have already seeded the data with playerId = 1L and their availability
        long playerId = 1L;

        mockMvc.perform(get("/api/playersAvailability?playerId={playerId}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerId").value(1L))
                .andExpect(jsonPath("$[0].tournamentId").isNotEmpty())
                .andExpect(jsonPath("$[0].startTime").isNotEmpty())
                .andExpect(jsonPath("$[0].endTime").isNotEmpty())
                .andExpect(jsonPath("$[0].available").isNotEmpty());
    }

    @Test
    public void testGetPlayerAvailabilityByTournamentId() throws Exception {
        // Assume you have already seeded the data with tournamentId = 1L
        long tournamentId = 1L;

        mockMvc.perform(get("/api/playersAvailability?tournamentId={tournamentId}", tournamentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tournamentId").value(1L))
                .andExpect(jsonPath("$[0].playerId").isNotEmpty())
                .andExpect(jsonPath("$[0].startTime").isNotEmpty())
                .andExpect(jsonPath("$[0].endTime").isNotEmpty())
                .andExpect(jsonPath("$[0].available").isNotEmpty());
    }

    @Test
    public void testBulkCreatePlayerAvailability() throws Exception {

        // Set properties for mockAvailability if necessary
        PlayerAvailability mockAvailability = new PlayerAvailability(
                1L,
                1L,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                true
        );

        PlayerAvailability mockAvailability2 = new PlayerAvailability(
                1L,
                2L,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                true
        );

        List<PlayerAvailability> availabilities = new ArrayList<>();
        availabilities.add(mockAvailability);
        availabilities.add(mockAvailability2);

        mockMvc.perform(post("/api/playersAvailability/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availabilities)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[1].tournamentId").value(2L))
                .andExpect(jsonPath("$[1].playerId").isNotEmpty())
                .andExpect(jsonPath("$[1].startTime").isNotEmpty())
                .andExpect(jsonPath("$[1].endTime").isNotEmpty())
                .andExpect(jsonPath("$[1].available").isNotEmpty());
    }
}
