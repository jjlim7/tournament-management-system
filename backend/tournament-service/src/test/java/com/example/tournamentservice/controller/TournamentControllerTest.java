package com.example.tournamentservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.exception.TournamentsNotFoundException;
import com.example.tournamentservice.service.TournamentService;

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

    @SuppressWarnings("null")
    @Test
    public void testGetAllTournaments() {
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.getAllTournaments()).thenReturn(Collections.singletonList(tournament));

        ResponseEntity<List<Tournament>> response = tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(tournamentService, times(1)).getAllTournaments();
    }

    @Test
    public void testGetTournamentById() {
        Long tournamentId = 1L;
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.getTournamentById(tournamentId)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.getTournamentById(tournamentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tournament, response.getBody());
        verify(tournamentService, times(1)).getTournamentById(tournamentId);
    }

    @Test
    public void testGetTournamentById_NotFound() {
        Long tournamentId = 1L;
        when(tournamentService.getTournamentById(tournamentId)).thenThrow(new TournamentsNotFoundException("Tournament cannot be found"));

        TournamentsNotFoundException exception = assertThrows(TournamentsNotFoundException.class, () -> {
            tournamentController.getTournamentById(tournamentId);
        });

        assertEquals("Tournament cannot be found", exception.getMessage());
    }

    @Test
    public void testDeleteTournament() {
        Long tournamentId = 1L;
        Long requestingAdminId = 100L;

        ResponseEntity<String> response = tournamentController.deleteTournament(tournamentId, requestingAdminId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tournamentService, times(1)).deleteTournament(tournamentId, requestingAdminId);
    }

    @Test
    public void testUpdateTournament() {
        Long tournamentId = 1L;
        Tournament tournament = new Tournament(); // Set properties as needed
        when(tournamentService.updateTournament(tournamentId, tournament)).thenReturn(tournament);

        ResponseEntity<String> response = tournamentController.updateTournament(tournamentId, tournament);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tournament updated successfully! ID: " + tournament.getTournament_id(), response.getBody());
        verify(tournamentService, times(1)).updateTournament(tournamentId, tournament);
    }

    @Test
    public void testJoinTournament() {
        Long tournamentId = 1L;
        Long playerId = 10L;
        String expectedResponse = "Player joined tournament successfully";
        
        when(tournamentService.joinTournament(tournamentId, playerId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = tournamentController.joinTournament(tournamentId, playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(tournamentService, times(1)).joinTournament(tournamentId, playerId);
    }

    @Test
    public void testLeaveTournament() {
        Long tournamentId = 1L;
        Long playerId = 10L;
        String expectedResponse = "Player left tournament successfully";
        
        when(tournamentService.leaveTournament(tournamentId, playerId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = tournamentController.leaveTournament(tournamentId, playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(tournamentService, times(1)).leaveTournament(tournamentId, playerId);
    }
}
