package com.example.userservice.controller;

// import org.hibernate.mapping.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.service.*;
import com.example.userservice.entity.*;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.*;




@RestController
public class ClanUserController {
    private ClanUserService clanUserService;

    public ClanUserController(ClanUserService clanUserService) {
        this.clanUserService = clanUserService;
    }

    @GetMapping("/clanusers")
    public List<ClanUser> getAllClanUsers() {
        return clanUserService.listAllClanUsers();
    }

    @GetMapping("/clanusers/{clanuserId}")
    public ClanUser getClanUser(@PathVariable Long clanUserId) {
        ClanUser clanUser = clanUserService.getClanUser(clanUserId);

        if(clanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return clanUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clanusers")
    public ClanUser addClanUser(@RequestBody ClanUser clanUser) {
        return clanUserService.addClanUser(clanUser);
    }

    @PutMapping("/clanusers/{id}")
    public ClanUser updateClanUserDetails(@PathVariable Long clanUserId, @RequestBody ClanUser newClanUser) {
        ClanUser updatedClanUser = clanUserService.updateClanUser(clanUserId, newClanUser);

        if(updatedClanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return updatedClanUser;
    }

    @DeleteMapping("/clanusers/{id}")
    public void deleteClanUser(@PathVariable Long clanUserId) {

        try {
            clanUserService.deleteClanUser(clanUserId);
        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(clanUserId);
        }
    }
    
    
}
