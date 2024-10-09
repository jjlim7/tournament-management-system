package com.example.userservice.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClanUserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClanUserNotFoundException(Long clanUserId) {
        super("Could not find Clan User with ClanUserId: " + clanUserId);
    }


}
