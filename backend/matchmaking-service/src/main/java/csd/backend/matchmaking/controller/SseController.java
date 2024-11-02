package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Tournament;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SseController {

    private final List<SseEmitter> emitters = new ArrayList<>();

    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    public SseController() {
        // Start a heartbeat task to keep connections alive
        heartbeatExecutor.scheduleAtFixedRate(this::sendHeartbeat, 15, 15, TimeUnit.SECONDS);
    }

    /**
     * Send a heartbeat to all connected clients to keep the connection alive.
     */
    private void sendHeartbeat() {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data("keep-alive"));
            } catch (IOException e) {
                failedEmitters.add(emitter);
            }
        });
        emitters.removeAll(failedEmitters);
    }

    /**
     * Endpoint for clients to connect and receive SSE notifications.
     */
    @GetMapping("/api/notifications/stream")
    public SseEmitter streamEvents() {
        // One hour timeout
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitters.add(emitter);

        // Remove the emitter when the connection is complete
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    /**
     * Method to broadcast an event to all connected clients.
     */
    public void sendEventToClients(String message) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("cronEvent").data(message));
            } catch (IOException e) {
                failedEmitters.add(emitter);
            }
        });
        // Remove failed emitters (disconnected clients)
        emitters.removeAll(failedEmitters);
    }

    public void sendUpdatedTournamentsToClients(List<Tournament> tournaments) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("cronEvent").data(tournaments));
            } catch (IOException e) {
                failedEmitters.add(emitter);
            }
        });
        // Remove failed emitters (disconnected clients)
        emitters.removeAll(failedEmitters);
    }
}