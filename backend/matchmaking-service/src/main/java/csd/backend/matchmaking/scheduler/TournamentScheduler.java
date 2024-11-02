package csd.backend.matchmaking.scheduler;

import csd.backend.matchmaking.feigndto.Tournament;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TournamentScheduler {

    @Autowired
    private TournamentClient tournamentClient;

    @Autowired
    private MatchmakingService matchmakingService;

    @Autowired
    private TaskScheduler taskScheduler;  // Injecting the TaskScheduler

    public List<Tournament> scheduleUpcomingTournaments() {
        List<Tournament> tournaments = tournamentClient.getUpcomingTournaments();
        for (Tournament tournament : tournaments) {
            scheduleTournament(tournament);
        }
        System.out.printf("Successfully scheduled upcoming tournaments %s%n", tournaments.stream().map(Tournament::getTournament_id).toList());
        return tournaments;
    }

    /**
     * Method to schedule games 1 hour before the tournament and update the status when it starts.
     */
    private void scheduleTournament(Tournament tournament) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startTime = tournament.getStartDate();

        tournamentClient.updateTournamentStatus(tournament.getTournament_id(), "SCHEDULED");

        // Schedule status update to 'ONGOING' when the tournament starts
        long delayForStatusUpdate = ChronoUnit.MILLIS.between(now, startTime);  // Delay for the exact start time
        taskScheduler.schedule(
                () -> updateTournamentStatusToActive(tournament),
                new java.util.Date(System.currentTimeMillis() + delayForStatusUpdate)
        );
        System.out.printf("%s :: Scheduled status update for tournament %s at %s%n", now, tournament.getTournament_id(), startTime);

        // Schedule game creation 1 hour before the tournament starts
        long delayForGameCreation = ChronoUnit.MILLIS.between(now, startTime.minusHours(1));  // Delay for 1 hour before
        taskScheduler.schedule(
                () -> triggerGameScheduling(tournament),
                new java.util.Date(System.currentTimeMillis() + delayForGameCreation)
        );
        System.out.printf("%s :: Scheduled game creation for tournament %s at %s%n", now, tournament.getTournament_id(), startTime.minusHours(1));

    }

    /**
     * Trigger the game scheduling 1 hour before the tournament starts.
     */
    private void triggerGameScheduling(Tournament tournament) {
        try {
            if (tournament.getGameMode() == Tournament.GameMode.BATTLE_ROYALE) {
                matchmakingService.scheduleBattleRoyaleGames(tournament.getTournament_id());
            } else if (tournament.getGameMode() == Tournament.GameMode.CLAN_WAR) {
                matchmakingService.scheduleClanWarGames(tournament.getTournament_id());
            }
            System.out.println("Games scheduled for tournament " + tournament.getTournament_id());
        } catch (Exception e) {
            System.err.println("Error scheduling games for tournament " + tournament.getTournament_id() + ": " + e.getMessage());
        }
    }

    /**
     * Update the tournament status to 'ACTIVE' when the tournament starts.
     */
    private void updateTournamentStatusToActive(Tournament tournament) {
        try {
            tournamentClient.updateTournamentStatus(tournament.getTournament_id(), Tournament.Status.ACTIVE.toString());
            System.out.println("Tournament " + tournament.getTournament_id() + " is now ACTIVE.");
        } catch (Exception e) {
            System.err.println("Error updating status for tournament " + tournament.getTournament_id() + ": " + e.getMessage());
        }
    }
}
