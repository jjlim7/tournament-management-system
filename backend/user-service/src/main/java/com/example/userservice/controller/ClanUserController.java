package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.service.*;
import com.example.userservice.dto.Request;
import com.example.userservice.entity.*;
import com.example.userservice.exceptions.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.*;
import io.swagger.v3.oas.annotations.Operation;




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

    @Operation(summary = "Get all clanusers", description = "Returns a list of clanusers")
    @GetMapping("/clanusers")
    public List<ClanUser> getAllClanUsers() {
        return clanUserService.listAllClanUsers();
    }

    @Operation(summary = "Get a specific clanUser", description = "Returns a specific clanUser with a specific Clan User Id")
    @GetMapping("/clanusers/user/{clanUserId}")
    public ClanUser getClanUser(@PathVariable Long clanUserId) {
        ClanUser clanUser = clanUserService.getClanUser(clanUserId);

        if(clanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return clanUser;
    }

    @Operation(summary="Get all users from clan", description = "Returns a list of clan users under specific clan")
    @GetMapping("/clanusers/clan/{clanId}")
    public List<ClanUser> getClanUsersByClan(@PathVariable Long clanId) {
        Clan clan = clanService.getClan(clanId);
        if(clan == null) {
            throw new ClanNotFoundException();
        }
        return clanUserService.listAllClanUsersFromClan(clanId);
    }

    @Operation(summary = "Add a new Clan User", description = "Returns a new Clan User with relevant details")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clanusers")
    @Transactional
    public ClanUser addClanUser(@Valid @RequestBody Request.CreateClanUser clanUser) {
        // System.out.println(clanUser);
        
        // create a new clan and user objects here
        Long clanId = clanUser.getClanId();
        Clan clan = clanService.getClan(clanId);
        if(clan == null) {
            throw new ClanNotFoundException();
        }

        Long userId = clanUser.getUserId();
        User user = userService.getUser(userId);
        if(user == null) {
            throw new UserNotFoundException();
        }

        ClanUser properClanUser = new ClanUser(user, clan, clanUser.getIsClanLeader(), clanUser.getPosition());

        return clanUserService.addClanUser(properClanUser);
    }

    @Operation(summary = "Updates a Clan User", description = "Returns the Clan User with updated Clan User details")
    @PutMapping("/clanusers/{clanUserId}")
    public ClanUser updateClanUserDetails(@PathVariable Long clanUserId, @RequestBody Request.UpdateClanUser newClanUserInfo) {

        Long newClanId = newClanUserInfo.getClanId();
        if(newClanId == null) {
            throw new ClanUserNotFoundException();
        }
        Clan newClan = clanService.getClan(newClanId);
        if(newClan == null) {
            throw new ClanNotFoundException(newClanId);
        }

        Long newUserId = newClanUserInfo.getUserId();
        if(newUserId == null) {
            throw new ClanUserNotFoundException();
        }
        User newUser = userService.getUser(newUserId);
        if(newUser == null) {
            throw new UserNotFoundException(newUserId);
        }

        ClanUser newClanUser = new ClanUser(newUser, newClan, newClanUserInfo.getIsClanLeader(), newClanUserInfo.getPosition());

        ClanUser updatedClanUser = clanUserService.updateClanUser(clanUserId, newClanUser);

        // System.out.println(updatedClanUser);

        if(updatedClanUser == null) {
            throw new ClanUserNotFoundException(clanUserId);
        }

        return updatedClanUser;
    }

    @Operation(summary = "Deletes a specific Clan User", description = "Deletes Clan User based on its Clan User Id")
    @DeleteMapping("/clanusers/{clanUserId}")
    public void deleteClanUser(@PathVariable Long clanUserId) {

        try {
            clanUserService.deleteClanUser(clanUserId);
        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(clanUserId);
        }
    }


    @Operation(summary = "Get clan users if they exist", description = "")
    @GetMapping("/clanusers/search")
    public ResponseEntity<ClanUser> getClanUserIfExists(@RequestParam Long userId) {
        ClanUser clanUser = clanUserService.getClanUserByUserId(userId);
        if (clanUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clanUser);
    }


}
