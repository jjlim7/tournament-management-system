package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.services.ClanAvailabilityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClanAvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClanAvailabilityControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClanAvailabilityService clanAvailabilityService;

    @Test
    void createClanAvailability_success() throws Exception {
        // Mock the ClanAvailability object
        ClanAvailability clanAvailability = new ClanAvailability(
                1L, 100L, 200L,
                OffsetDateTime.parse("2024-01-01T10:00:00.000Z"),
                OffsetDateTime.parse("2024-01-01T12:00:00.000Z"),
                true
        );

        // Mock service call
        when(clanAvailabilityService.createClanAvailability(any(ClanAvailability.class)))
                .thenReturn(clanAvailability);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/clanAvailability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clanId\":100,\"tournamentId\":200,\"startTime\":\"2024-01-01T10:00:00.000Z\",\"endTime\":\"2024-01-01T12:00:00.000Z\",\"available\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clanId").value(100L))
                .andExpect(jsonPath("$.tournamentId").value(200L))
                .andExpect(jsonPath("$.startTime").value("2024-01-01T10:00:00.000Z"))
                .andExpect(jsonPath("$.endTime").value("2024-01-01T12:00:00.000Z"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void bulkCreateClanAvailabilities_success() throws Exception {
        // Mock the list of ClanAvailability objects
        List<ClanAvailability> clanAvailabilities = Arrays.asList(
                new ClanAvailability(1L, 100L, 200L, OffsetDateTime.parse("2024-01-01T10:00:00.000Z"), OffsetDateTime.parse("2024-01-01T12:00:00.000Z"), true),
                new ClanAvailability(2L, 101L, 201L, OffsetDateTime.parse("2024-01-02T10:00:00.000Z"), OffsetDateTime.parse("2024-01-02T12:00:00.000Z"), true)
        );

        // Mock service call
        when(clanAvailabilityService.bulkCreateClanAvailabilities(any(List.class)))
                .thenReturn(clanAvailabilities);

        // Perform the POST request and verify the response
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
    }

    @Test
    void getClanAvailabilitiesByClanId_success() throws Exception {
        // Mock the list of ClanAvailability objects for a specific clanId
        List<ClanAvailability> clanAvailabilities = Arrays.asList(
                new ClanAvailability(1L, 100L, 200L, OffsetDateTime.parse("2024-01-01T10:00:00.000Z"), OffsetDateTime.parse("2024-01-01T12:00:00.000Z"), true)
        );

        // Mock service call
        when(clanAvailabilityService.getClanAvailabilitiesByClanId(100L)).thenReturn(clanAvailabilities);

        // Perform the GET request and verify the response
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
    void getClanAvailabilitiesByTournamentId_success() throws Exception {
        // Mock the list of ClanAvailability objects for a specific tournamentId
        List<ClanAvailability> clanAvailabilities = Arrays.asList(
                new ClanAvailability(1L, 100L, 200L, OffsetDateTime.parse("2024-01-01T10:00:00.000Z"), OffsetDateTime.parse("2024-01-01T12:00:00.000Z"), true)
        );

        // Mock service call
        when(clanAvailabilityService.getClanAvailabilitiesByTournamentId(200L)).thenReturn(clanAvailabilities);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/clanAvailability")
                        .param("tournamentId", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clanId").value(100L))
                .andExpect(jsonPath("$[0].tournamentId").value(200L))
                .andExpect(jsonPath("$[0].startTime").value("2024-01-01T10:00:00.000Z"))
                .andExpect(jsonPath("$[0].endTime").value("2024-01-01T12:00:00.000Z"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void getClanAvailabilitiesByClanAndTournament_success() throws Exception {
        // Mock the list of ClanAvailability objects for a specific clanId and tournamentId
        List<ClanAvailability> clanAvailabilities = Arrays.asList(
                new ClanAvailability(1L, 100L, 200L, OffsetDateTime.parse("2024-01-01T10:00:00.000Z"), OffsetDateTime.parse("2024-01-01T12:00:00.000Z"), true)
        );

        // Mock service call
        when(clanAvailabilityService.getClanAvailabilitiesByClanAndTournament(100L, 200L)).thenReturn(clanAvailabilities);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/clanAvailability")
                        .param("clanId", "100")
                        .param("tournamentId", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clanId").value(100L))
                .andExpect(jsonPath("$[0].tournamentId").value(200L))
                .andExpect(jsonPath("$[0].startTime").value("2024-01-01T10:00:00.000Z"))
                .andExpect(jsonPath("$[0].endTime").value("2024-01-01T12:00:00.000Z"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void deleteClanAvailability_success() throws Exception {
        // Perform the DELETE request and verify the response
        mockMvc.perform(delete("/api/clanAvailability/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verify the service method is called
        Mockito.verify(clanAvailabilityService, Mockito.times(1)).deleteClanAvailability(1L);
    }
}
