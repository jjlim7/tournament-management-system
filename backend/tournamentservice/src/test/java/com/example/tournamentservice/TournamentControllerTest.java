package com.example.tournamentservice;

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
    public void setup1(){
        tournament = new Tournament();
        tournament.setTournament_id(1L);
        tournament.setName("Test tournament");
        tournament.setDescription("123");
    }

    @Test
    public void testCreateTournament() throws Exception {
        String tournamentJson = "{\"name\": \"Test Tournament\", \"description\": \"123\"}";

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

        mockMvc.perform(get("/api/tournaments/tournaments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test tournament"))
                .andExpect(jsonPath("$[0].description").value("123"));
    }

    @Test
    public void testUpdateTournament() throws Exception {
        Tournament updatedTournament = new Tournament();
        updatedTournament.setTournament_id(1L);
        updatedTournament.setName("Updated Tournament");
        updatedTournament.setDescription("Updated Description");

        when(tournamentService.updateTournament(anyLong(), any(Tournament.class))).thenReturn(updatedTournament);

        String tournamentJson = "{\"name\": \"Updated Tournament\", \"description\": \"Updated Description\"}";

        mockMvc.perform(put("/api/tournaments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tournamentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Tournament"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testDeleteTournament() throws Exception {
        mockMvc.perform(delete("/api/tournaments/1"))
            .andExpect(status().isNoContent());
    }
    
}

    
