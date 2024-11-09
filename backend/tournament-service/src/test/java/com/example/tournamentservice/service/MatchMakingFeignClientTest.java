package com.example.tournamentservice.service;

import com.example.tournamentservice.DTO.EntityResponseDTO;
import com.example.tournamentservice.DTO.GameDTO;
import com.example.tournamentservice.DTO.PlayerAvailabilityDTO;
import com.example.tournamentservice.entity.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchMakingFeignClientTest {

    @Mock
    private MatchMakingFeignClient matchMakingFeignClient;

    @InjectMocks
    private TournamentService tournamentService; // Assuming you have a service that uses this Feign client

    private GameDTO gameDTO;
    private List<Long> playerIds;
    private PlayerAvailabilityDTO playerAvailabilityDTO;

    @BeforeEach
    public void setup() {
        gameDTO = new GameDTO(); // Initialize with mock data
        gameDTO.setId(1L);
        gameDTO.setGameMode(Tournament.GameMode.BATTLE_ROYALE);
        
        playerIds = List.of(100L, 101L); // Mock player IDs
        
        playerAvailabilityDTO = new PlayerAvailabilityDTO(); // Initialize with mock data
        playerAvailabilityDTO.setPlayerId(100L);
        playerAvailabilityDTO.setAvailable(true);
    }


    @Test
    public void testGetPlayerIdsByGame() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.getPlayerIdsByGame(anyLong())).thenReturn(playerIds);

        // Call the method
        List<Long> result = matchMakingFeignClient.getPlayerIdsByGame(1L);

        // Verify the response
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(100L));
        verify(matchMakingFeignClient, times(1)).getPlayerIdsByGame(anyLong());
    }



    @Test
    public void testGetGamesByTournament() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.getGamesByTournament(anyLong())).thenReturn(List.of(gameDTO));

        // Call the method
        List<GameDTO> result = matchMakingFeignClient.getGamesByTournament(1L);

        // Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(matchMakingFeignClient, times(1)).getGamesByTournament(anyLong());
    }

    @Test
    public void testGetGameById() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.getGameById(anyLong())).thenReturn(gameDTO);

        // Call the method
        GameDTO result = matchMakingFeignClient.getGameById(1L);

        // Verify the response
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(matchMakingFeignClient, times(1)).getGameById(anyLong());
    }

    @Test
    public void testGetGamePlayerOrClanIds() {
        EntityResponseDTO.EntityIdResponse entityIdResponse = new EntityResponseDTO.EntityIdResponse(); // Mock data
        when(matchMakingFeignClient.getGamePlayerOrClanIds(anyLong())).thenReturn(entityIdResponse);

        EntityResponseDTO.EntityIdResponse result = matchMakingFeignClient.getGamePlayerOrClanIds(1L);

        assertNotNull(result);
        verify(matchMakingFeignClient, times(1)).getGamePlayerOrClanIds(anyLong());
    }

    @Test
    public void testScheduleGames() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.scheduleGames(anyLong(), any())).thenReturn(List.of(gameDTO));

        // Call the method
        List<GameDTO> result = matchMakingFeignClient.scheduleGames(1L, Tournament.GameMode.BATTLE_ROYALE);

        // Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(matchMakingFeignClient, times(1)).scheduleGames(anyLong(), any());
    }

    @Test
    public void testGroupAvailabilitiesByStartTime() {
        List<PlayerAvailabilityDTO> availabilities = List.of(playerAvailabilityDTO);
        OffsetDateTime currentTime = OffsetDateTime.now(); // Capture the current time
    
        Map<OffsetDateTime, List<PlayerAvailabilityDTO>> expectedMap = new HashMap<>();
        expectedMap.put(currentTime, availabilities);
    
        // Mock the Feign client's response
        when(matchMakingFeignClient.groupAvailabilitiesByStartTime(any())).thenReturn(expectedMap);
    
        // Call the method
        Map<OffsetDateTime, List<PlayerAvailabilityDTO>> result = matchMakingFeignClient.groupAvailabilitiesByStartTime(availabilities);
    
        // Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(currentTime)); // Use the captured current time
        verify(matchMakingFeignClient, times(1)).groupAvailabilitiesByStartTime(any());
    }

    @Test
    public void testGetPlayerAvailabilitiesByPlayerId() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.getPlayerAvailabilitiesByPlayerId(anyLong())).thenReturn(List.of(playerAvailabilityDTO));

        // Call the method
        List<PlayerAvailabilityDTO> result = matchMakingFeignClient.getPlayerAvailabilitiesByPlayerId(100L);

        // Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getPlayerId());
        verify(matchMakingFeignClient, times(1)).getPlayerAvailabilitiesByPlayerId(anyLong());
    }

    @Test
    public void testGetPlayerAvailabilitiesByTournamentId() {
        // Mock the Feign client's response
        when(matchMakingFeignClient.getPlayerAvailabilitiesByTournamentId(anyLong())).thenReturn(List.of(playerAvailabilityDTO));

        // Call the method
        List<PlayerAvailabilityDTO> result = matchMakingFeignClient.getPlayerAvailabilitiesByTournamentId(1L);

        // Verify the response
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getPlayerId());
        verify(matchMakingFeignClient, times(1)).getPlayerAvailabilitiesByTournamentId(anyLong());
    }
}
