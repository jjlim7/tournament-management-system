package csd.backend.matchmaking.integration;

import com.example.tournamentservice.entity.Tournament;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.scheduler.TournamentCronTrigger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TournamentCronTriggerTest {

    @Autowired
    private TournamentCronTrigger tournamentCronTrigger;

    @MockitoBean
    private TournamentClient tournamentClient;

    @Test
    void testCronJobManually() {
        when(tournamentClient.getUpcomingTournaments())
                .thenReturn(Collections.singletonList(createMockTournament()));

        // Manually invoke the cron job method
        tournamentCronTrigger.checkAndScheduleUpcomingTournaments();

        // Add assertions or verifications based on expected behavior
        // For example, check if games are scheduled or if the scheduler was invoked.
    }

    private Tournament createMockTournament() {
        // Create a mock TournamentDto object
        Tournament tournament = new Tournament();
        tournament.setTournament_id(1L);
        tournament.setName("Mock Tournament");
        tournament.setStartDate(OffsetDateTime.now().plusDays(1));
        tournament.setEndDate(OffsetDateTime.now().plusDays(2));
        tournament.setStatus(Tournament.Status.INACTIVE);
        tournament.setGameMode(Tournament.GameMode.BATTLE_ROYALE);
        tournament.setAdminId(123L);
        tournament.setPlayerIds(List.of(1L, 2L, 3L));
        return tournament;
    }
}
