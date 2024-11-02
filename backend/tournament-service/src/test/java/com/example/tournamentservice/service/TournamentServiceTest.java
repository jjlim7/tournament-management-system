// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
// import com.example.tournamentservice.entity.Tournament;
// import com.example.tournamentservice.repository.TournamentRepository;
// import com.example.tournamentservice.service.TournamentService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.time.OffsetDateTime;
// import java.util.Optional;

// class TournamentServiceTest {

//     @Mock
//     private TournamentRepository tournamentRepository;

//     @InjectMocks
//     private TournamentService tournamentService;

//     private Tournament tournament;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.initMocks(this);

//         tournament = new Tournament();
//         tournament.setName("Test Tournament");
//         tournament.setStartDate(OffsetDateTime.now().plusDays(1));
//         tournament.setEndDate(OffsetDateTime.now().plusDays(2));
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

//     // You can continue to add more tests for other scenarios and methods...
// }
