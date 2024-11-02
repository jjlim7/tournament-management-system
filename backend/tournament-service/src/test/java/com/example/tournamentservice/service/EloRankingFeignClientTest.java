package com.example.tournamentservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tournamentservice.DTO.ClanEloRankDTO;
import com.example.tournamentservice.DTO.PlayerEloRankDTO;

@ExtendWith(MockitoExtension.class)
public class EloRankingFeignClientTest {

    @Mock
    private EloRankingFeignClient eloRankingFeignClient;

    @InjectMocks
    private EloRankingFeignClientTest client;

    @BeforeEach
    void setUp() {
        // You can initialize the mocks if needed
    }

    @Test
    public void testGetClanEloRank() {
        Long clanId = 1L;
        Long tournamentId = 1L;
        ClanEloRankDTO mockClanEloRank = new ClanEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getClanEloRank(clanId, tournamentId)).thenReturn(Optional.of(mockClanEloRank));

        Optional<ClanEloRankDTO> result = eloRankingFeignClient.getClanEloRank(clanId, tournamentId);
        assertTrue(result.isPresent());
        assertEquals(mockClanEloRank, result.get());

        // Test negative case
        when(eloRankingFeignClient.getClanEloRank(clanId, tournamentId)).thenReturn(Optional.empty());
        result = eloRankingFeignClient.getClanEloRank(clanId, tournamentId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetPlayerEloRank() {
        Long playerId = 1L;
        Long tournamentId = 1L;
        PlayerEloRankDTO mockPlayerEloRank = new PlayerEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId)).thenReturn(Optional.of(mockPlayerEloRank));

        Optional<PlayerEloRankDTO> result = eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId);
        assertTrue(result.isPresent());
        assertEquals(mockPlayerEloRank, result.get());

        // Test negative case
        when(eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId)).thenReturn(Optional.empty());
        result = eloRankingFeignClient.getPlayerEloRank(playerId, tournamentId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetClanEloRanksByTournament() {
        Long tournamentId = 1L;
        ClanEloRankDTO mockClanEloRank1 = new ClanEloRankDTO(); // Set properties as needed
        ClanEloRankDTO mockClanEloRank2 = new ClanEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getClanEloRanksByTournament(tournamentId))
            .thenReturn(Arrays.asList(mockClanEloRank1, mockClanEloRank2));

        List<ClanEloRankDTO> result = eloRankingFeignClient.getClanEloRanksByTournament(tournamentId);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockClanEloRank1));
        assertTrue(result.contains(mockClanEloRank2));

        // Test negative case
        when(eloRankingFeignClient.getClanEloRanksByTournament(tournamentId)).thenReturn(Collections.emptyList());
        result = eloRankingFeignClient.getClanEloRanksByTournament(tournamentId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllPlayerEloRanksByTournament() {
        Long tournamentId = 1L;
        PlayerEloRankDTO mockPlayerEloRank1 = new PlayerEloRankDTO(); // Set properties as needed
        PlayerEloRankDTO mockPlayerEloRank2 = new PlayerEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId))
            .thenReturn(Arrays.asList(mockPlayerEloRank1, mockPlayerEloRank2));

        List<PlayerEloRankDTO> result = eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockPlayerEloRank1));
        assertTrue(result.contains(mockPlayerEloRank2));

        // Test negative case
        when(eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId)).thenReturn(Collections.emptyList());
        result = eloRankingFeignClient.getAllPlayerEloRanksByTournament(tournamentId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSelectedPlayerEloRanksByTournament() {
        Long tournamentId = 1L;
        List<Long> playerIds = Arrays.asList(1L, 2L);
        PlayerEloRankDTO mockPlayerEloRank1 = new PlayerEloRankDTO(); // Set properties as needed
        PlayerEloRankDTO mockPlayerEloRank2 = new PlayerEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds))
            .thenReturn(Arrays.asList(mockPlayerEloRank1, mockPlayerEloRank2));

        List<PlayerEloRankDTO> result = eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockPlayerEloRank1));
        assertTrue(result.contains(mockPlayerEloRank2));

        // Test negative case
        when(eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds)).thenReturn(Collections.emptyList());
        result = eloRankingFeignClient.getSelectedPlayerEloRanksByTournament(tournamentId, playerIds);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPlayerEloRanksByRatingRange() {
        Long tournamentId = 1L;
        double minRating = 1000;
        double maxRating = 2000;
        PlayerEloRankDTO mockPlayerEloRank1 = new PlayerEloRankDTO(); // Set properties as needed
        PlayerEloRankDTO mockPlayerEloRank2 = new PlayerEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating))
            .thenReturn(Arrays.asList(mockPlayerEloRank1, mockPlayerEloRank2));

        List<PlayerEloRankDTO> result = eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockPlayerEloRank1));
        assertTrue(result.contains(mockPlayerEloRank2));

        // Test negative case
        when(eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating)).thenReturn(Collections.emptyList());
        result = eloRankingFeignClient.getPlayerEloRanksByRatingRange(tournamentId, minRating, maxRating);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetClanEloRanksByRatingRange() {
        Long tournamentId = 1L;
        double minRating = 1000;
        double maxRating = 2000;
        ClanEloRankDTO mockClanEloRank1 = new ClanEloRankDTO(); // Set properties as needed
        ClanEloRankDTO mockClanEloRank2 = new ClanEloRankDTO(); // Set properties as needed

        when(eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating))
            .thenReturn(Arrays.asList(mockClanEloRank1, mockClanEloRank2));

        List<ClanEloRankDTO> result = eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockClanEloRank1));
        assertTrue(result.contains(mockClanEloRank2));

        // Test negative case
        when(eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating)).thenReturn(Collections.emptyList());
        result = eloRankingFeignClient.getClanEloRanksByRatingRange(tournamentId, minRating, maxRating);
        assertTrue(result.isEmpty());
    }
}
