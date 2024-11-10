package com.example.tournamentservice.service;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.Status;
import com.example.tournamentservice.exception.TournamentsNotFoundException;
import com.example.tournamentservice.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        OffsetDateTime referenceDate = OffsetDateTime.parse("2024-11-01T00:00:00+08:00");
        
        tournament = new Tournament();
        tournament.setTournament_id(1L);
        tournament.setName("Sample Tournament");
        tournament.setPlayerCapacity(10);
        tournament.setStartDate(referenceDate.plusDays(2));  // Start date 1 day after reference date
        tournament.setEndDate(referenceDate.plusDays(4));    // End date 2 days after reference date
        tournament.setStatus(Status.INACTIVE);
    }
    

    @Test
    public void testCreateTournament_ValidTournament_ShouldSaveAndReturnTournament() {
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament result = tournamentService.createTournament(tournament);

        assertNotNull(result);
        assertEquals(Status.INACTIVE, result.getStatus());
        verify(tournamentRepository, times(1)).save(tournament);
    }

    // @Test
    // public void testCreateTournament_InvalidDates_ShouldThrowException() {
    //     OffsetDateTime referenceDate = OffsetDateTime.parse("2024-11-01T00:00:00+08:00");

    //     // Set start date to a day before the reference date and end date after the reference date, creating an invalid scenario
    //     tournament.setStartDate(referenceDate.minusDays(5)); // Invalid as per the business logic
    //     tournament.setEndDate(referenceDate.plusDays(1));

    //     assertThrows(IllegalArgumentException.class, () -> tournamentService.createTournament(tournament));
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }


    @Test
    public void testGetAllTournaments_WhenTournamentsExist_ShouldReturnSortedTournaments() {
        when(tournamentRepository.findAll(
            Sort.by(Sort.Direction.ASC, "status").and(Sort.by(Sort.Direction.ASC, "startDate"))
        )).thenReturn(List.of(tournament));

        List<Tournament> tournaments = tournamentService.getAllTournaments();

        assertFalse(tournaments.isEmpty());
        assertEquals("Sample Tournament", tournaments.get(0).getName());
        verify(tournamentRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    public void testGetAllTournaments_NoTournamentsFound_ShouldThrowException() {
        when(tournamentRepository.findAll(
            Sort.by(Sort.Direction.ASC, "status").and(Sort.by(Sort.Direction.ASC, "startDate"))
        )).thenReturn(Collections.emptyList());

        assertThrows(TournamentsNotFoundException.class, () -> tournamentService.getAllTournaments());
    }

    @Test
    public void testGetTournamentById_ExistingTournament_ShouldReturnTournament() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        Tournament result = tournamentService.getTournamentById(1L);

        assertNotNull(result);
        assertEquals("Sample Tournament", result.getName());
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTournamentById_NonExistingTournament_ShouldThrowException() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TournamentsNotFoundException.class, () -> tournamentService.getTournamentById(1L));
    }

    @Test
    public void testUpdateTournament_ValidUpdate_ShouldReturnUpdatedTournament() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament updatedTournament = new Tournament();
        updatedTournament.setName("Updated Tournament");

        Tournament result = tournamentService.updateTournament(1L, updatedTournament);

        assertEquals("Updated Tournament", result.getName());
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    public void testUpdateTournament_ActiveTournament_ShouldThrowException() {
        tournament.setStatus(Status.ACTIVE);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        Tournament updatedTournament = new Tournament();
        updatedTournament.setName("Updated Tournament");

        assertThrows(IllegalStateException.class, () -> tournamentService.updateTournament(1L, updatedTournament));
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    public void testDeleteTournament_AdminCanDelete_ShouldDeleteTournament() {
        tournament.setAdminId(1L);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        tournamentService.deleteTournament(1L, 1L);

        verify(tournamentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTournament_NonAdminAttemptToDelete_ShouldThrowException() {
        tournament.setAdminId(1L);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        assertThrows(SecurityException.class, () -> tournamentService.deleteTournament(1L, 2L));
        verify(tournamentRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testJoinTournament_PlayerNotRegistered_ShouldAddPlayer() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        String result = tournamentService.joinTournament(1L, 100L);

        assertEquals("Player joined the tournament successfully.", result);
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    public void testJoinTournament_TournamentFull_ShouldThrowException() {
        tournament.setPlayerCapacity(1);
        tournament.getPlayerIds().add(100L);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        assertThrows(IllegalArgumentException.class, () -> tournamentService.joinTournament(1L, 101L));
    }

    @Test
    public void testLeaveTournament_PlayerRegistered_ShouldRemovePlayer() {
        tournament.getPlayerIds().add(100L);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        String result = tournamentService.leaveTournament(1L, 100L);

        assertEquals("Player with ID 100 left the tournament successfully.", result);
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    public void testLeaveTournament_PlayerNotRegistered_ShouldThrowException() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));

        assertThrows(IllegalArgumentException.class, () -> tournamentService.leaveTournament(1L, 100L));
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }
}
