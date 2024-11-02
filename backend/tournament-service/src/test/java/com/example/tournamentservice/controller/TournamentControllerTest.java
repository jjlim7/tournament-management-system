package com.example.tournamentservice.controller;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TournamentControllerTest {

    @InjectMocks
    private TournamentController tournamentController;

    @Mock
    private TournamentService tournamentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTournament() {
        Tournament tournament = new Tournament(); // Set properties as needed

        ResponseEntity<String> response = tournamentController.createTournament(tournament);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tournament created successfully!", response.getBody());

        verify(tournamentService, times(1)).createTournament(tournament);
    }

    // @Test
    // public void testCreateTournament_InvalidInput() throws Exception {
    //     Tournament tournament = new Tournament(); // Populate with invalid data that violates validation constraints

    //     mockMvc.perform(post("/create")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(tournament)))
    //             .andExpect(status().isBadRequest());
    // }

    @Test
    public void testGetAllTournaments() {
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.getAllTournaments()).thenReturn(Collections.singletonList(tournament));

        ResponseEntity<List<Tournament>> response = tournamentController.getAllTournament();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(tournamentService, times(1)).getAllTournaments();
    }

    @Test
    public void testGetTournamentById() {
        Long tournamentId = 1L;
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.getTournamentById(tournamentId)).thenReturn(Optional.of(tournament));

        ResponseEntity<Tournament> response = tournamentController.getTournamentById(tournamentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
        verify(tournamentService, times(1)).getTournamentById(tournamentId);
    }

    @Test
    public void testGetTournamentById_NotFound() {
        Long tournamentId = 1L;
        when(tournamentService.getTournamentById(tournamentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tournamentController.getTournamentById(tournamentId);
        });

        assertEquals("Tournament cannot be found", exception.getMessage());
    }

    @Test
    public void testDeleteTournament() {
        Long tournamentId = 1L;

        ResponseEntity<Void> response = tournamentController.deleteTournament(tournamentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tournamentService, times(1)).deleteTournament(tournamentId);
    }

    @Test
    public void testUpdateTournament() {
        Long tournamentId = 1L;
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.updateTournament(tournamentId, tournament)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.updateTournament(tournamentId, tournament);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
        verify(tournamentService, times(1)).updateTournament(tournamentId, tournament);
    }

    // Add more tests as needed for joinTournament and leaveTournament
}
