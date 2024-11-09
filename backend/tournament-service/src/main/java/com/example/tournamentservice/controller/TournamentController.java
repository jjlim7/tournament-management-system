package com.example.tournamentservice.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.tournamentservice.DTO.ClanEloRankDTO;
import com.example.tournamentservice.DTO.EntityResponseDTO;
import com.example.tournamentservice.DTO.GameDTO;
import com.example.tournamentservice.DTO.PlayerAvailabilityDTO;
import com.example.tournamentservice.DTO.PlayerEloRankDTO;
import com.example.tournamentservice.dataloader.TournamentDataLoader;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.exception.TournamentsNotFoundException;
import com.example.tournamentservice.service.EloRankingFeignClient;
import com.example.tournamentservice.service.MatchMakingFeignClient;
import com.example.tournamentservice.service.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://your-frontend-domain.com") //adjust accordingly
@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private EloRankingFeignClient eloRankingFeignClient;

    @Autowired
    private MatchMakingFeignClient matchMakingFeignClient;

    @Autowired
    private TournamentDataLoader tournamentDataLoader;

    @Operation(summary = "Creation of Bulk Data", description = "Seeding data",tags = {"Others"})
    @PostMapping("/load-from-csv")
    public String loadTournamentsFromCsv() {
        try {
            tournamentDataLoader.loadTournamentsFromCsv();
            return "Tournament data loaded successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to load tournament data: " + e.getMessage();
        }
    }

    //Operations for Tournaments
    @Operation(summary = "Create a tournament", description = "Create a new tournament",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tournament created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createTournament(@Valid @RequestBody Tournament tournament) {
        try {
            tournamentService.createTournament(tournament);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tournament created successfully!");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating tournament: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating tournament.");
        }
    }

    @Operation(summary = "Get all tournaments", description = "Retrieve a list of all tournaments.",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tournaments"),
        @ApiResponse(responseCode = "204", description = "No tournaments found")
    })
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Get tournament by ID", description = "Retrieve a tournament by its ID.",tags = {"Tournament Filter Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tournament"),
        @ApiResponse(responseCode = "404", description = "Tournament not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Get tournament by name", description = "Retrieve a tournament by its name.",tags = {"Tournament Filter Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tournament"),
        @ApiResponse(responseCode = "404", description = "Tournament not found with the given name")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Tournament> getTournamentByName(@PathVariable String name) {
        Tournament tournament = tournamentService.getTournamentByName(name);
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Get tournaments by date range", description = "Retrieve tournaments within a specified date range.",tags = {"Tournament Filter Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tournaments"),
        @ApiResponse(responseCode = "404", description = "No tournaments found in the given date range")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Tournament>> getTournamentsByDateRange(
            @RequestParam OffsetDateTime startDate,
            @RequestParam OffsetDateTime endDate) {
        List<Tournament> tournaments = tournamentService.getTournamentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Update a tournament", description = "Update a tournament by its ID",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "409", description = "Conflict: Cannot edit an active tournament")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        try {
            Tournament updatedTournament = tournamentService.updateTournament(id, tournament);
            return ResponseEntity.ok("Tournament updated successfully! ID: " + updatedTournament.getTournament_id());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (TournamentsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating tournament: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete a tournament", description = "Delete a tournament by its ID",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tournament deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden: Only the tournament creator can delete this tournament")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable Long id, @RequestParam Long requestingAdminId) {
        try {
            tournamentService.deleteTournament(id, requestingAdminId);
            return ResponseEntity.noContent().build();
        } catch (TournamentsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + id + " not found.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only the tournament creator can delete this tournament.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting tournament: " + e.getMessage());
        }
    }

    @Operation(summary = "Join a tournament", description = "Allow a player to join a tournament",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player joined tournament successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "400", description = "Player cannot join the tournament due to invalid conditions")
    })
    @PostMapping("/{tournament_id}/join/{player_id}")
    public ResponseEntity<String> joinTournament(@PathVariable Long tournament_id, @PathVariable Long player_id) {
        try {
            String result = tournamentService.joinTournament(tournament_id, player_id);
            return ResponseEntity.ok(result);
        } catch (TournamentsNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + tournament_id + " not found.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @Operation(summary = "Leave a tournament", description = "Allows a player to leave a tournament.",tags = {"Tournament Basic Operations"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player left tournament successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "400", description = "Player is not part of this tournament")
    })
    @PostMapping("/{tournament_id}/leave/{player_id}")
    public ResponseEntity<String> leaveTournament(@PathVariable Long tournament_id, @PathVariable Long player_id) {
        try {
            String result = tournamentService.leaveTournament(tournament_id, player_id);
            return ResponseEntity.ok(result);
        } catch (TournamentsNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + tournament_id + " not found.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @Operation(summary = "Get Clan Elo Rank", description = "Retrieve the Elo rank of a clan in a specified tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clan Elo rank retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Clan or Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching the clan Elo rank")
    })
    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<Optional<ClanEloRankDTO>> getClanEloRank(
            @PathVariable Long clanId,
            @PathVariable Long tournamentId
    ) {
        Optional<ClanEloRankDTO> clanEloRank = eloRankingFeignClient.getClanEloRank(clanId, tournamentId);
        return ResponseEntity.ok(clanEloRank);
    }

    @Operation(summary = "Get Player Elo Rank", description = "Retrieve the Elo rank of a player in a specified tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player Elo rank retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Player or Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching the player Elo rank")
    })
    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<Optional<PlayerEloRankDTO>> getPlayerEloRank(
            @PathVariable Long playerId,
            @PathVariable Long tournamentId
    ) {
        Optional<PlayerEloRankDTO> playerEloRank = eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId);
        return ResponseEntity.ok(playerEloRank);
    }

    @Operation(summary = "Get Clan Elo Ranks by Tournament", description = "Retrieve all clan Elo ranks for a specified tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clan Elo ranks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching clan Elo ranks")
    })
    @GetMapping("/clan/tournament/{tournamentId}")
    public ResponseEntity<List<ClanEloRankDTO>> getClanEloRanksByTournament(
            @PathVariable Long tournamentId
    ) {
        List<ClanEloRankDTO> clanEloRanks = eloRankingFeignClient.getClanEloRanksByTournament(tournamentId);
        return ResponseEntity.ok(clanEloRanks);
    }

    @Operation(summary = "Get All Player Elo Ranks by Tournament", description = "Retrieve all player Elo ranks for a specified tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player Elo ranks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player Elo ranks")
    })
    @GetMapping("/player/tournament/{tournamentId}")
    public ResponseEntity<List<PlayerEloRankDTO>> getAllPlayerEloRanksByTournament(
            @PathVariable Long tournamentId
    ) {
        List<PlayerEloRankDTO> playerEloRanks = eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId);
        return ResponseEntity.ok(playerEloRanks);
    }

    @Operation(summary = "Get Selected Player Elo Ranks by Tournament", description = "Retrieve Elo ranks of selected players in a specified tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Selected player Elo ranks retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament or Players not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching selected player Elo ranks")
    })
    @GetMapping("/player/tournament/{tournamentId}/selected")
    public ResponseEntity<List<PlayerEloRankDTO>> getSelectedPlayerEloRanksByTournament(
            @PathVariable Long tournamentId,
            @RequestParam List<Long> playerIds
    ) {
        List<PlayerEloRankDTO> selectedPlayerEloRanks = eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds);
        return ResponseEntity.ok(selectedPlayerEloRanks);
    }

    @Operation(summary = "Get Player Elo Ranks by Rating Range", description = "Retrieve player Elo ranks within a specified rating range for a tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player Elo ranks by rating range retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player Elo ranks by rating range")
    })
    @GetMapping("/player/tournament/{tournamentId}/rating-range")
    public ResponseEntity<List<PlayerEloRankDTO>> getPlayerEloRanksByRatingRange(
            @PathVariable Long tournamentId,
            @RequestParam double minRating,
            @RequestParam double maxRating
    ) {
        List<PlayerEloRankDTO> playerEloRanks = eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating);
        return ResponseEntity.ok(playerEloRanks);
    }

    @Operation(summary = "Get Clan Elo Ranks by Rating Range", description = "Retrieve clan Elo ranks within a specified rating range for a tournament.", tags = {"IPC - Elo Ranking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clan Elo ranks by rating range retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching clan Elo ranks by rating range")
    })
    @GetMapping("/clan/tournament/{tournamentId}/rating-range")
    public ResponseEntity<List<ClanEloRankDTO>> getClanEloRanksByRatingRange(
            @PathVariable Long tournamentId,
            @RequestParam double minRating,
            @RequestParam double maxRating
    ) {
        List<ClanEloRankDTO> clanEloRanks = eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating);
        return ResponseEntity.ok(clanEloRanks);
    }

    @Operation(summary = "Get Player IDs by Game", description = "Retrieve a list of player IDs associated with a specified game.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player IDs retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Game not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player IDs")
    })
    @GetMapping("/games/{gameId}/players")
    public List<Long> getPlayerIdsByGame(@PathVariable Long gameId) {
        return matchMakingFeignClient.getPlayerIdsByGame(gameId);
    }

    @Operation(summary = "Get Games by Tournament", description = "Retrieve a list of games associated with a specified tournament.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Games retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching games")
    })
    @GetMapping("/tournaments/{tournamentId}/games")
    public List<GameDTO> getGamesByTournament(@PathVariable Long tournamentId) {
        return matchMakingFeignClient.getGamesByTournament(tournamentId);
    }

    @Operation(summary = "Get Game by ID", description = "Retrieve details of a specific game using its ID.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Game not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching the game details")
    })
    @GetMapping("/games/{gameId}")
    public GameDTO getGameById(@PathVariable Long gameId) {
        return matchMakingFeignClient.getGameById(gameId);
    }

    @Operation(summary = "Get Game Player or Clan IDs", description = "Retrieve player or clan IDs associated with a specified game.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player or clan IDs retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Game not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player or clan IDs")
    })
    @GetMapping("/games/{gameId}/player-or-clan-ids")
    public EntityResponseDTO.EntityIdResponse getGamePlayerOrClanIds(@PathVariable long gameId) {
        return matchMakingFeignClient.getGamePlayerOrClanIds(gameId);
    }

    @Operation(summary = "Schedule Games", description = "Schedule games for a specified tournament based on the game mode.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Games scheduled successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "400", description = "Invalid game mode provided"),
        @ApiResponse(responseCode = "500", description = "An error occurred while scheduling games")
    })
    @PostMapping("/tournaments/{tournamentId}/games/schedule")
    public List<GameDTO> scheduleGames(
            @PathVariable long tournamentId,
            @RequestParam Tournament.GameMode gameMode) {
        return matchMakingFeignClient.scheduleGames(tournamentId, gameMode);
    }

    @Operation(summary = "Group Availabilities by Start Time", description = "Group player availabilities by their start time.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availabilities grouped by start time successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid availabilities list provided"),
        @ApiResponse(responseCode = "500", description = "An error occurred while grouping availabilities")
    })
    @PostMapping("/availabilities/group-by-start")
    public Map<OffsetDateTime, List<PlayerAvailabilityDTO>> groupAvailabilitiesByStartTime(
            @RequestBody List<PlayerAvailabilityDTO> availabilities) {
        return matchMakingFeignClient.groupAvailabilitiesByStartTime(availabilities);
    }

    @Operation(summary = "Create Game", description = "Create a new game for a specified tournament with the given start and end times.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid game parameters provided"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while creating the game")
    })
    @PostMapping("/games/schedule")
    public GameDTO createGame(
            @RequestParam long tournamentId,
            @RequestParam OffsetDateTime startTime,
            @RequestParam OffsetDateTime endTime,
            @RequestParam Tournament.GameMode gameMode,
            @RequestBody List<PlayerAvailabilityDTO> availablePlayers) {
        return matchMakingFeignClient.createGame(tournamentId, startTime, endTime, gameMode, availablePlayers);
    }

    @Operation(summary = "Get Player Availabilities by Player ID", description = "Retrieve availabilities for a specified player.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player availabilities retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player availabilities")
    })
    @GetMapping("/players/{playerId}/availabilities")
    public List<PlayerAvailabilityDTO> getPlayerAvailabilitiesByPlayerId(@PathVariable long playerId) {
        return matchMakingFeignClient.getPlayerAvailabilitiesByPlayerId(playerId);
    }

    @Operation(summary = "Get Player Availabilities by Tournament ID", description = "Retrieve availabilities for players in a specified tournament.", tags = {"IPC - Matchmaking"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player availabilities retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "500", description = "An error occurred while fetching player availabilities")
    })
    @GetMapping("/tournaments/{tournamentId}/availabilities")
    public List<PlayerAvailabilityDTO> getPlayerAvailabilitiesByTournamentId(@PathVariable long tournamentId) {
        return matchMakingFeignClient.getPlayerAvailabilitiesByTournamentId(tournamentId);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Tournament>> getUpcomingTournaments() {
        List<Tournament> upcomingTournaments = tournamentService.findUpcomingTournaments();
        return new ResponseEntity<>(upcomingTournaments, HttpStatus.OK);
    }

    @PutMapping("/{tournamentId}/status")
    public ResponseEntity<Void> updateTournamentStatus(@PathVariable("tournamentId") Long tournamentId, @RequestParam("newStatus") String newStatus) {
        // Convert the newStatus string to the Tournament.Status enum
        try {
            Tournament.Status statusEnum = Tournament.Status.valueOf(newStatus.toUpperCase());
            boolean updated = tournamentService.updateTournamentStatus(tournamentId, statusEnum);
            if (updated) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            // Return BAD_REQUEST if the status is not valid
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
