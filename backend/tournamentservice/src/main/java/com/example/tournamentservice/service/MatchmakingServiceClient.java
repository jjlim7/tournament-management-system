// package com.example.tournamentservice.service;

// import java.util.List;

// import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.example.tournamentservice.entity.Tournament;
// import com.example.tournamentservice.entity.Tournament.Status;


// import com.example.tournamentservice.DTO.GameDTO; // Make sure this is the correct path to GameDTO
// import com.example.tournamentservice.DTO.PlayerAvailabilityDTO; // Adjusted import for PlayerAvailabilityDTO

// @FeignClient(name = "matchmaking-service", url = "${MATCHMAKING_SERVICE_URL}")
// public interface MatchmakingServiceClient {

//     @PostMapping("/api/games")
//     ResponseEntity<GameDTO> createGame(@RequestBody GameDTO game);

//     @PutMapping("/api/games/{gameId}/status")
//     ResponseEntity<GameDTO> updateGameStatus(@PathVariable("gameId") Long gameId,
//                                               @RequestBody Tournament.Status gameStatus); // Adjusted to match enum type

//     @GetMapping("/api/games")
//     ResponseEntity<List<GameDTO>> getGamesByTournament(@RequestParam("tournamentId") Long tournamentId);

//     @GetMapping("/api/games/{gameId}")
//     ResponseEntity<GameDTO> getGameById(@PathVariable("gameId") Long gameId);

//     @GetMapping("/api/games/{gameId}/playerIds")
//     ResponseEntity<List<Long>> getPlayerIdsByGame(@PathVariable("gameId") Long gameId);

//     @GetMapping("/api/games/{gameId}/clanIds")
//     ResponseEntity<List<Long>> getClanIdsByGame(@PathVariable("gameId") Long gameId);

//     //@GetMapping("/api/games/{gameId}/playerOrClanIds")
//     //ResponseEntity<Response.EntityIdResponse> getPlayerOrClanIdsByGame(@PathVariable("gameId") Long gameId);

//     @PostMapping("/api/matchmaking/schedule-games")
//     ResponseEntity<String> scheduleGames(@RequestParam("tournamentId") long tournamentId);

//     @PostMapping("/api/playersAvailability")
//     ResponseEntity<PlayerAvailabilityDTO> createPlayerAvailability(@RequestBody PlayerAvailabilityDTO playerAvailability);

//     @PostMapping("/api/playersAvailability/bulk")
//     ResponseEntity<List<PlayerAvailabilityDTO>> bulkCreatePlayerAvailabilities(@RequestBody List<PlayerAvailabilityDTO> playerAvailabilities);

//     @GetMapping(value = "/api/playersAvailability", params = "playerId")
//     ResponseEntity<List<PlayerAvailabilityDTO>> getPlayerAvailabilitiesByPlayerId(@RequestParam("playerId") long playerId);

//     @GetMapping(value = "/api/playersAvailability", params = "tournamentId")
//     ResponseEntity<List<PlayerAvailabilityDTO>> findPlayerAvailabilities(@RequestParam("tournamentId") long tournamentId);

//     String getMatchmakingInfo(Long tournamentId);
// }
