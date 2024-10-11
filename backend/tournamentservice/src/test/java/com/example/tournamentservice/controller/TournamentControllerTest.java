package com.example.tournamentservice.controller;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import com.example.tournamentservice.controller.TournamentController;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TournamentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;
   
    @MockBean
    private TournamentService tournamentService;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        tournament = new Tournament();
        tournament.setTournament_id(1L);
        tournament.setName("Test Tournament");
        tournament.setDescription("This is a test tournament.");
        tournament.setStartDate(OffsetDateTime.now().plusDays(1));
        tournament.setEndDate(OffsetDateTime.now().plusDays(2));
        tournament.setPlayerCapacity(100);
        tournament.setStatus(Tournament.Status.Active);
        tournament.setGameMode(Tournament.GameMode.Royale);
        tournament.setAdminId(1L); // Mock admin ID
    }

    @Test
    public void testCreateTournament() throws Exception {
        String tournamentJson = """
            {
                "name": "Test Tournament",
                "description": "This is a test tournament.",
                "startDate": "2024-10-09T10:00:00Z",
                "endDate": "2024-10-10T10:00:00Z",
                "playerCapacity": 100,
                "status": "Active",
                "gameMode": "Royale",
                "adminId": 1
            }
        """;

        when(tournamentService.createTournament(any(Tournament.class))).thenReturn(tournament);
        
        mockMvc.perform(post("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tournamentJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Tournament created successfully!"));
    }

    @Test
    public void testGetAllTournaments() throws Exception {
        List<Tournament> tournaments = Arrays.asList(tournament);

        when(tournamentService.getAllTournaments()).thenReturn(tournaments);

        mockMvc.perform(get("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Tournament"))
                .andExpect(jsonPath("$[0].description").value("This is a test tournament."))
                .andExpect(jsonPath("$[0].playerCapacity").value(100))
                .andExpect(jsonPath("$[0].status").value("Active"))
                .andExpect(jsonPath("$[0].gameMode").value("Royale"));
    }

    @Test
    public void testUpdateTournament() throws Exception {
        Tournament updatedTournament = new Tournament();
        updatedTournament.setTournament_id(1L);
        updatedTournament.setName("Updated Tournament");
        updatedTournament.setDescription("Updated Description");
        updatedTournament.setStartDate(OffsetDateTime.now().plusDays(1));
        updatedTournament.setEndDate(OffsetDateTime.now().plusDays(3));
        updatedTournament.setPlayerCapacity(150);
        updatedTournament.setStatus(Tournament.Status.Active);
        updatedTournament.setGameMode(Tournament.GameMode.ClanWar);
        updatedTournament.setAdminId(1L); // Mock admin ID

        when(tournamentService.updateTournament(anyLong(), any(Tournament.class))).thenReturn(updatedTournament);

        String tournamentJson = """
            {
                "name": "Updated Tournament",
                "description": "Updated Description",
                "startDate": "2024-10-10T10:00:00Z",
                "endDate": "2024-10-12T10:00:00Z",
                "playerCapacity": 150,
                "status": "Active",
                "gameMode": "ClanWar",
                "adminId": 1
            }
        """;

        mockMvc.perform(put("/api/tournaments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tournamentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Tournament"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.playerCapacity").value(150))
                .andExpect(jsonPath("$.gameMode").value("ClanWar"));
    }

    @Test
    public void testDeleteTournament() throws Exception {
        mockMvc.perform(delete("/api/tournaments/1"))
            .andExpect(status().isNoContent());
    }
    
}

    
