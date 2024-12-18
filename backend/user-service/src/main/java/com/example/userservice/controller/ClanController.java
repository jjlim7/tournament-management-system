package com.example.userservice.controller;

import com.example.userservice.feigndto.ClanEloRank;
import com.example.userservice.feigndto.PlayerEloRank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.*;
import com.example.userservice.service.*;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

import com.example.userservice.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class ClanController {
    @Autowired
    private ClanService clanService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public ClanController(ClanService clanService) {
        this.clanService = clanService;
    }

    // Read
    @Operation(summary = "Get all clans", description = "Returns a list of clans")
    @GetMapping("/clans")
    public List<Clan> getAllClans() {
        return clanService.listAllClans();
    }

    @Operation(summary = "Gets a specific clan", description = "Returns a specific clan with a specific clan id")
    @GetMapping("/clans/{clanId}")
    public Clan getClan(@PathVariable Long clanId) {
        Clan clan = clanService.getClan(clanId);

        if(clan == null) {
            throw new ClanNotFoundException(clanId);
        } 

        return clan;
    }

    // Create
    @Operation(summary = "Add a new clan", description = "Returns a new clan with the relevant details")
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 - success
    @PostMapping("/clans") // post mapping to add the new clan to the entire list of clans
    @Transactional
    public Clan addClan(@RequestBody Clan clan) {
        return clanService.addClan(clan);
    }

    // Update
    @Operation(summary = "Updates a clan", description = "Returns the clan with updated clan details")
    @PutMapping("/clans/{clanId}") // PUT is to update
    public Clan updateClanDetails(@PathVariable Long clanId, @RequestBody Clan newClan) {
        Clan updatedClan = clanService.updateClan(clanId, newClan);
        
        if(updatedClan == null) {
            throw new ClanNotFoundException(clanId);
        }

        return updatedClan;
    }

    // Delete
    @Operation(summary = "Deletes a specific clan", description = "Deletes clans based on their clan id")
    @DeleteMapping("/clans/{clanId}")
    public void deleteClan(@PathVariable Long clanId) {
        
        try {
            clanService.deleteClan(clanId);

        } catch(EmptyResultDataAccessException e) {
            throw new ClanNotFoundException(clanId);
        }
    }

    // complex ms fn that calls elo-ranking-service
    @Operation(summary = "Get elo rank for specific clan", description = "Get latest/current elo ranking of clan")
    @GetMapping("/clans/{clanId}/latest-rank")
    public ResponseEntity<?> getClanEloRank(@PathVariable Long clanId) {
        try {
            ClanEloRank rank = clanService.getLatestClanEloRank(clanId);
            return ResponseEntity.ok(rank);
        } catch (Exception e) {
// Log the exception for debugging
            log.error("Failed to retrieve Elo rank for clan: {}", clanId, e);
            // Return a meaningful error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve Elo rank at this time.");
        }
    }

    @Operation(summary = "get clan details", description = "")
    @GetMapping("/clans/{clanId}/overall")
    public ResponseEntity<?> getClanOverall(@PathVariable Long clanId) {
        try {
            ClanStats clanStatsLatest = clanService.getClanStats(clanId);
            return ResponseEntity.ok(clanStatsLatest);
        } catch (Exception e) {
            log.error("Failed to retrieve clan details for clan: {}", clanId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve user details for clan.");
        }
    }

}
