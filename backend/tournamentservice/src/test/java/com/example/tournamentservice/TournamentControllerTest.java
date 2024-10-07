package com.example.tournamentservice;

import java.util.Arrays;
import java.util.List;
import com.example.tournamentservice.controller.TournamentController;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TournamentController.class)
@ActiveProfiles("test")
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;
   
    @MockBean
    private TournamentService tournamentService;

    private Tournament tournament1;
    private Tournament tournament2;

    // @BeforeEach
    // public void setup(){
    //     tournament = new Tournament();
    //     tournament.setTournament_id(1L);
    //     tournament.setName("Test tournament");
    //     tournament.setDescription("123");
    // }

    @BeforeEach
    public void setup() {
        tournament1 = new Tournament();
        tournament1.setTournament_id(1L);
        tournament1.setName("Tournament 1");
        tournament1.setDescription("Description 1");

        tournament2 = new Tournament();
        tournament2.setTournament_id(2L);
        tournament2.setName("Tournament 2");
        tournament2.setDescription("Description 2");
    }

    //Seems to be working.
    @WithMockUser(roles = "USER")
    @Test
    public void testGetAllTournaments() throws Exception {
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

        when(tournamentService.getAllTournaments()).thenReturn(tournaments);

        mockMvc.perform(get("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"tournament_id\":1,\"name\":\"Tournament 1\",\"description\":\"Description 1\"}," +
                        "{\"tournament_id\":2,\"name\":\"Tournament 2\",\"description\":\"Description 2\"}]"));
    }

    // @Test
    // public void testUpdateTournament() throws Exception {
    //     Tournament updatedTournament = new Tournament();
    //     updatedTournament.setTournament_id(1L);
    //     updatedTournament.setName("Updated Tournament");
    //     updatedTournament.setDescription("Updated Description");

    //     Mockito.when(tournamentService.updateTournament(anyLong(), any(Tournament.class))).thenReturn(updatedTournament);

    //     mockMvc.perform(put("/api/tournaments/1")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content("{\"name\": \"Updated Tournament\", \"description\": \"Updated Description\"}"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.name").value("Updated Tournament"))
    //             .andExpect(jsonPath("$.description").value("Updated Description"));
    // }

    @Test
    public void testDeleteTournament() throws Exception {
        mockMvc.perform(delete("/api/tournaments/1"))
         .andExpect(status().isNoContent());
  }

}

    
