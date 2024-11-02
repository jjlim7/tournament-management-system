package csd.backend.matchmaking.integration;

import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback after each test to avoid side effects
class ClanAvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClanAvailabilityRepository clanAvailabilityRepository;

    @BeforeEach
    void setup() {
        // Clean up the database before each test
        clanAvailabilityRepository.deleteAll();
    }

    @Test
    void createClanAvailability_success() throws Exception {
        // Perform POST request to create ClanAvailability
        mockMvc.perform(post("/api/clanAvailability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clanId\":100,\"tournamentId\":200,\"startTime\":\"2024-01-01T10:00:00.000Z\",\"endTime\":\"2024-01-01T12:00:00.000Z\",\"available\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clanId").value(100L))
                .andExpect(jsonPath("$.tournamentId").value(200L))
                .andExpect(jsonPath("$.startTime").value("2024-01-01T10:00:00.000Z"))
                .andExpect(jsonPath("$.endTime").value("2024-01-01T12:00:00.000Z"))
                .andExpect(jsonPath("$.available").value(true));

        // Verify that the availability was saved in the database
        ClanAvailability savedAvailability = clanAvailabilityRepository.findAll().get(0);
        assert savedAvailability.getClanId().equals(100L);
        assert savedAvailability.getTournamentId().equals(200L);
        assert savedAvailability.isAvailable();
    }

    @Test
    void getClanAvailabilitiesByClanId_success() throws Exception {
        // Insert test data into the database
        ClanAvailability clanAvailability = new ClanAvailability(
                null, 100L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        clanAvailabilityRepository.save(clanAvailability);

        // Perform GET request and verify the response
        mockMvc.perform(get("/api/clanAvailability")
                        .param("clanId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clanId").value(100L))
                .andExpect(jsonPath("$[0].tournamentId").value(200L))
                .andExpect(jsonPath("$[0].startTime").value("2024-01-01T10:00:00.000Z"))
                .andExpect(jsonPath("$[0].endTime").value("2024-01-01T12:00:00.000Z"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void bulkCreateClanAvailabilities_success() throws Exception {
        // Perform POST request to bulk create ClanAvailability
        mockMvc.perform(post("/api/clanAvailability/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[" +
                                "{\"clanId\":100,\"tournamentId\":200,\"startTime\":\"2024-01-01T10:00:00.000Z\",\"endTime\":\"2024-01-01T12:00:00.000Z\",\"available\":true}," +
                                "{\"clanId\":101,\"tournamentId\":201,\"startTime\":\"2024-01-02T10:00:00.000Z\",\"endTime\":\"2024-01-02T12:00:00.000Z\",\"available\":true}" +
                                "]"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].clanId").value(100L))
                .andExpect(jsonPath("$[0].tournamentId").value(200L))
                .andExpect(jsonPath("$[1].clanId").value(101L))
                .andExpect(jsonPath("$[1].tournamentId").value(201L));

        // Verify that the availabilities were saved in the database
        List<ClanAvailability> savedAvailabilities = clanAvailabilityRepository.findAll();
        assert savedAvailabilities.size() == 2;
        assert savedAvailabilities.get(0).getClanId().equals(100L);
        assert savedAvailabilities.get(1).getClanId().equals(101L);
    }

    @Test
    void deleteClanAvailability_success() throws Exception {
        // Insert test data into the database
        ClanAvailability clanAvailability = new ClanAvailability(
                null, 100L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );
        ClanAvailability savedAvailability = clanAvailabilityRepository.save(clanAvailability);

        // Perform DELETE request and verify response
        mockMvc.perform(delete("/api/clanAvailability/{id}", savedAvailability.getId()))
                .andExpect(status().isNoContent());

        // Verify that the availability was deleted from the database
        assert clanAvailabilityRepository.findById(savedAvailability.getId()).isEmpty();
    }
}
