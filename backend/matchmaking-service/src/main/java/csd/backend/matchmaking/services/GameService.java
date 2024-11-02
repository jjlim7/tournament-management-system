package csd.backend.matchmaking.services;

import csd.backend.matchmaking.dto.Response;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game createGame(Game.GameMode gameMode, long tournamentId, List<Long> playerIds, List<Long> clanIds, OffsetDateTime startTime, OffsetDateTime endTime, Game.GameStatus gameStatus) {
        switch (gameMode) {
            case BATTLE_ROYALE -> {
                return createBattleRoyaleGame(tournamentId, playerIds, startTime, endTime, gameStatus);
            }
            case CLAN_WAR -> {
                return createClanWarGame(tournamentId, clanIds, startTime, endTime, gameStatus);
            }
        }
        return null;
    }

    public Game createBattleRoyaleGame(long tournamentId, List<Long> playerIds, OffsetDateTime startTime, OffsetDateTime endTime, Game.GameStatus gameStatus) {
        Game game = new Game(tournamentId, playerIds, null, startTime, endTime, Game.GameMode.BATTLE_ROYALE, gameStatus);
        return gameRepository.save(game);
    }

    public Game createClanWarGame(long tournamentId, List<Long> clanIds, OffsetDateTime startTime, OffsetDateTime endTime, Game.GameStatus gameStatus) {
        Game game = new Game(tournamentId, null, clanIds, startTime, endTime, Game.GameMode.CLAN_WAR, gameStatus);
        return gameRepository.save(game);
    }

    public List<Long> getPlayerIdsByGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        return game.getPlayerIds();
    }

    public Game updateGameStatus(Long gameId, Game.GameStatus status) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            game.get().setGameStatus(status);
            return gameRepository.save(game.get());
        }
        throw new RuntimeException("Game not found");
    }

    public List<Game> getGamesByTournament(Long tournamentId) {
        return gameRepository.findAllByTournamentId(tournamentId);
    }

    public Game getGameById(Long gameId) {
        Optional<Game> exists = gameRepository.findById(gameId);
        return exists.orElseThrow(() -> new ResourceNotFoundException("Game not found"));
    }

    public Response.EntityIdResponse getGamePlayerOrClanIds(long gameId) {
        Game game = getGameById(gameId);

        return switch (game.getGameMode()) {
            case BATTLE_ROYALE -> new Response.EntityIdResponse("Player", game.getPlayerIds());
            case CLAN_WAR -> new Response.EntityIdResponse("Clan", game.getClanIds());
            default -> throw new IllegalArgumentException("Unsupported game mode: " + game.getGameMode());
        };
    }
}
