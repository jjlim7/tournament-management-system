package com.example.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClanNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClanNotFoundException (Long clanId) {
        super("Could not find Clan with ClanId: " + clanId);
    }

    public ClanNotFoundException () {
        super("Could not find Clan");
    }
}
