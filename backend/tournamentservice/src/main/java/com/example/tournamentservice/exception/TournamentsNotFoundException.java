package com.example.tournamentservice.exception;

public class TournamentsNotFoundException extends RuntimeException {
    public TournamentsNotFoundException(String message) {
        super(message);
    }
}
