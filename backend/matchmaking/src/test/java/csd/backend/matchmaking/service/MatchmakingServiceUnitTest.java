package csd.backend.matchmaking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import csd.backend.matchmaking.controller.GameController;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.services.GameService;
import csd.backend.matchmaking.services.MatchmakingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@Transactional
public class MatchmakingServiceUnitTest {

    @Autowired
    private MatchmakingService matchmakingService;

    @Autowired
    private ObjectMapper objectMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testScheduleBattleRoyaleGames() throws Exception {
        List<Game> gameList = matchmakingService.scheduleGames(1L, Game.GameMode.BATTLE_ROYALE);

        assert !gameList.isEmpty();
    }
}
