package com.example.tournamentservice.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.tournamentservice.DTO.UserDTO;

class TournamentServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserDetails() {
        Long userId = 1L;
        UserDTO mockUser = new UserDTO();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");
        // Set other fields as necessary

        when(userServiceClient.getUserById(userId)).thenReturn(mockUser);

        UserDTO user = tournamentService.getUserDetails(userId);

        assertEquals("testuser", user.getUsername());
        // Add more assertions to verify other fields if needed
    }
}
// package com.example.tournamentservice.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// import java.time.OffsetDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import com.example.tournamentservice.entity.Tournament;
// import com.example.tournamentservice.exception.TournamentsNotFoundException;
// import com.example.tournamentservice.repository.TournamentRepository;

// class TournamentServiceTest {

//     @Mock
//     private TournamentRepository tournamentRepository;

//     @InjectMocks
//     private TournamentService tournamentService;

//     private Tournament tournament;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);

//         tournament = new Tournament();
//         tournament.setId(1L);
//         tournament.setName("Test Tournament");
//         tournament.setStartDate(OffsetDateTime.now().plusDays(1));
//         tournament.setEndDate(OffsetDateTime.now().plusDays(2));
//         tournament.setPlayerCapacity(10);
//         tournament.setPlayerIds(new ArrayList<>());
//     }

//     @Test
//     void testCreateTournamentSuccess() {
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         Tournament createdTournament = tournamentService.createTournament(tournament);

//         assertNotNull(createdTournament);
//         assertEquals("Test Tournament", createdTournament.getName());
//     }

//     @Test
//     void testCreateTournamentWithInvalidDates() {
//         tournament.setStartDate(OffsetDateTime.now().minusDays(1));

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.createTournament(tournament);
//         });

//         assertEquals("Start date must be in the future", exception.getMessage());
//     }

//     @Test
//     void testGetAllTournamentsSuccess() {
//         List<Tournament> tournaments = List.of(tournament);
//         when(tournamentRepository.findAll(any())).thenReturn(tournaments);

//         List<Tournament> result = tournamentService.getAllTournaments();

//         assertNotNull(result);
//         assertEquals(1, result.size());
//         assertEquals("Test Tournament", result.get(0).getName());
//     }

//     @Test
//     void testGetAllTournamentsNoTournamentsFound() {
//         when(tournamentRepository.findAll(any())).thenReturn(new ArrayList<>());

//         Exception exception = assertThrows(TournamentsNotFoundException.class, () -> {
//             tournamentService.getAllTournaments();
//         });

//         assertEquals("No tournaments found.", exception.getMessage());
//     }

//     @Test
//     void testGetTournamentByIdSuccess() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         Tournament result = tournamentService.getTournamentById(1L);

//         assertNotNull(result);
//         assertEquals("Test Tournament", result.getName());
//     }

//     @Test
//     void testGetTournamentByIdNotFound() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

//         Exception exception = assertThrows(TournamentsNotFoundException.class, () -> {
//             tournamentService.getTournamentById(1L);
//         });

//         assertEquals("Tournament with ID 1 not found.", exception.getMessage());
//     }

//     @Test
//     void testUpdateTournamentSuccess() {
//         Tournament updatedTournament = new Tournament();
//         updatedTournament.setName("Updated Tournament");
//         updatedTournament.setPlayerCapacity(15);

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         Tournament result = tournamentService.updateTournament(1L, updatedTournament);

//         assertNotNull(result);
//         assertEquals("Updated Tournament", result.getName());
//     }

//     @Test
//     void testUpdateTournamentNotFound() {
//         Tournament updatedTournament = new Tournament();
//         updatedTournament.setName("Updated Tournament");

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

//         Exception exception = assertThrows(RuntimeException.class, () -> {
//             tournamentService.updateTournament(1L, updatedTournament);
//         });

//         assertEquals("Tournament not found", exception.getMessage());
//     }

//     @Test
//     void testDeleteTournamentSuccess() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         tournamentService.deleteTournament(1L, 1L); // Assuming adminId is 1

//         verify(tournamentRepository, times(1)).deleteById(1L);
//     }

//     @Test
//     void testDeleteTournamentNotFound() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.deleteTournament(1L, 1L);
//         });

//         assertEquals("Tournament not found with ID: 1", exception.getMessage());
//     }

//     @Test
//     void testJoinTournamentSuccess() {
//         tournament.setPlayerIds(new ArrayList<>());

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         String result = tournamentService.joinTournament(1L, 123L);

//         assertEquals("Player joined the tournament successfully.", result);
//         assertTrue(tournament.getPlayerIds().contains(123L));
//     }

//     @Test
//     void testJoinTournamentAlreadyJoined() {
//         tournament.getPlayerIds().add(123L);

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.joinTournament(1L, 123L);
//         });

//         assertEquals("Player is already registered in this tournament.", exception.getMessage());
//     }

//     @Test
//     void testJoinTournamentCapacityReached() {
//         tournament.setPlayerCapacity(1);
//         tournament.getPlayerIds().add(123L);

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.joinTournament(1L, 456L);
//         });

//         assertEquals("Tournament has reached its player capacity.", exception.getMessage());
//     }

//     @Test
//     void testLeaveTournamentSuccess() {
//         tournament.getPlayerIds().add(123L);

//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         String result = tournamentService.leaveTournament(1L, 123L);

//         assertEquals("Player with ID 123 left the tournament successfully.", result);
//         assertFalse(tournament.getPlayerIds().contains(123L));
//     }

//     @Test
//     void testLeaveTournamentNotRegistered() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             tournamentService.leaveTournament(1L, 456L); // 456 is not registered
//         });

//         assertEquals("Player with ID 456 is not registered in this tournament.", exception.getMessage());
//     }
// }
