package csd.backend.matchmaking.scheduler;

import csd.backend.matchmaking.dto.Tournament;
import csd.backend.matchmaking.controller.SseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class TournamentCronTrigger {

    @Autowired
    private TournamentScheduler tournamentScheduler;

    @Autowired
    private SseController sseController;

    // This method will run every hour and schedule the jobs for upcoming tournaments
    @Scheduled(cron = "0 */5 * * * ?")  // Run every 5 minutes for testing. TODO: Change to 1-hour
    //@Scheduled(cron = "0 0 * * * ?")  // Run every hour
    public void checkAndScheduleUpcomingTournaments() {
        List<Tournament> tournamentList = tournamentScheduler.scheduleUpcomingTournaments();

        // Send SSE event to all clients connected to the SSE endpoint
        OffsetDateTime now = OffsetDateTime.now();
        sseController.sendEventToClients("Scheduled tournaments processed at " + now);
        sseController.sendUpdatedTournamentsToClients(tournamentList);

        System.out.printf("Scheduled tournaments processed at %s%n", now);
    }
}
