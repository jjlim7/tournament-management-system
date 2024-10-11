package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.service.*;
import com.example.userservice.dto.Request;
import com.example.userservice.entity.*;
import com.example.userservice.exceptions.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.*;




@RestController
public class ClanUserController {
    @Autowired
    private ClanUserService clanUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClanService clanService;

    public ClanUserController(ClanUserService clanUserService) {
        this.clanUserService = clanUserService;
    }

    @GetMapping("/clanusers")
    public List<ClanUser> getAllClanUsers() {
        return clanUserService.listAllClanUsers();
    }

    @GetMapping("/clanusers/{clanUserId}")
    public ClanUser getClanUser(@PathVariable Long clanUserId) {
        ClanUser clanUser = clanUserService.getClanUser(clanUserId);

        if(clanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return clanUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clanusers")
    @Transactional
    public ClanUser addClanUser(@Valid @RequestBody Request.CreateClanUser clanUser) {
        System.out.println(clanUser);
        
        // create a new clan and user objects here
        Long clanId = clanUser.getClanId();
        Clan clan = clanService.getClan(clanId);

        Long userId = clanUser.getClanId();
        User user = userService.getUser(userId);

        ClanUser properClanUser = new ClanUser(user, clan, clanUser.getIsClanLeader(), clanUser.getPosition());

        return clanUserService.addClanUser(properClanUser);
    }

    @PutMapping("/clanusers/{clanUserId}")
    public ClanUser updateClanUserDetails(@PathVariable Long clanUserId, @RequestBody Request.UpdateClanUser newClanUserInfo) {

        Long newClanId = newClanUserInfo.getClanId();
        if(newClanId == null) {
            throw new ClanUserNotFoundException();

        }
        Clan newClan = clanService.getClan(newClanId);

        Long newUserId = newClanUserInfo.getUserId();
        if(newUserId == null) {
            throw new ClanUserNotFoundException();

        }
        User newUser = userService.getUser(newUserId);

        ClanUser newClanUser = new ClanUser(newUser, newClan, newClanUserInfo.getIsClanLeader(), newClanUserInfo.getPosition());

        ClanUser updatedClanUser = clanUserService.updateClanUser(clanUserId, newClanUser);

        // System.out.println(updatedClanUser);

        if(updatedClanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return updatedClanUser;
    }

    @DeleteMapping("/clanusers/{clanUserId}")
    public void deleteClanUser(@PathVariable Long clanUserId) {

        try {
            clanUserService.deleteClanUser(clanUserId);
        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(clanUserId);
        }
    }
    
    
}
