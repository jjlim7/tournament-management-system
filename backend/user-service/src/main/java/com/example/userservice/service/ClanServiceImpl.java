package com.example.userservice.service;

import com.example.userservice.feignclient.EloRankingClient;
import com.example.userservice.feigndto.ClanEloRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.userservice.entity.*;
import com.example.userservice.repository.*;

import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class ClanServiceImpl implements ClanService {
    @Autowired
    private ClanRepository clanDB;

    @Autowired
    private EloRankingClient eloRankingClient;

    public ClanServiceImpl(ClanRepository clanDB, EloRankingClient eloRankingClient) {
        this.clanDB = clanDB;
        this.eloRankingClient = eloRankingClient;
    }

    @Override
    @Transactional
    public List<Clan> listAllClans() {
        return clanDB.findAll();
    }

    @Override
    @Transactional
    public Clan addClan(Clan clan) {
        return clanDB.save(clan);
    }

    @Override
    @Transactional
    public Clan getClan(Long clanId) {
        return clanDB.findById(clanId).orElse(null);
    }

    @Override
    @Transactional
    public Clan updateClan(Long clanId, Clan newClan) {
        Optional<Clan> optionalClan = clanDB.findById(clanId);

        if(optionalClan.isPresent()) {
            Clan currentClan = optionalClan.get();

            currentClan.setClanName(newClan.getClanName());

            return clanDB.save(currentClan);
        } else {
            return null;
        } 
    }

    // didn't use Optional here - hopefully it doesn't get me into trouble
    @Override
    @Transactional
    public void deleteClan(Long clanId) {
        clanDB.deleteById(clanId);
    }

    @Override
    @Transactional
    public ClanEloRank getLatestClanEloRank(Long clanId) {
        // guard statement
        Optional<Clan> optionalClan = clanDB.findById(clanId);
        if (optionalClan.isEmpty()) {
            return null;
        }
        ResponseEntity<Map<String, Object>> resp = eloRankingClient.getClanLatestRank(clanId);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        return (ClanEloRank) Objects.requireNonNull(resp.getBody()).get("data");
    }
}
