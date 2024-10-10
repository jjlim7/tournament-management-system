package com.example.userservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.*;
import com.example.userservice.service.*;

import jakarta.transaction.Transactional;

import com.example.userservice.exceptions.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class ClanController {
    @Autowired
    private ClanService clanService;

    public ClanController(ClanService clanService) {
        this.clanService = clanService;
    }

    // Read
    @GetMapping("/clans")
    public List<Clan> getAllClans() {
        return clanService.listAllClans();
    }

    @GetMapping("/clans/{clanId}")
    public Clan getClan(@PathVariable Long clanId) {
        Clan clan = clanService.getClan(clanId);

        if(clan == null) {
            throw new ClanNotFoundException(clanId);
        } 

        return clan;
    }

    // Create
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 - success
    @PostMapping("/clans") // post mapping to add the new clan to the entire list of clans
    @Transactional
    public Clan addClan(@RequestBody Clan clan) {
        return clanService.addClan(clan);
    }

    // Update
    @PutMapping("/clans/{clanId}") // PUT is to update
    public Clan updateClanDetails(@PathVariable Long clanId, @RequestBody Clan newClan) {
        Clan updatedClan = clanService.updateClan(clanId, newClan);
        
        if(updatedClan == null) {
            throw new ClanNotFoundException(clanId);
        }

        return updatedClan;
    }

    // Delete
    @DeleteMapping("/clans/{clanId}")
    public void deleteClan(@PathVariable Long clanId) {
        
        try {
            clanService.deleteClan(clanId);

        } catch(EmptyResultDataAccessException e) {
            throw new ClanNotFoundException(clanId);
        }
    }
        
}
