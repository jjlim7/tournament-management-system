package com.example.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClanUserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClanUserNotFoundException(Long clanUserId) {
        super("Could not find Clan User with ClanUserId: " + clanUserId);
    }


    public ClanUserNotFoundException() {
        super("Could not find the new Clan User being added");
    }
}
