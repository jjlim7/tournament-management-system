package com.example.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.userservice.entity.*;
import com.example.userservice.repository.*;

import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class ClanServiceImpl implements ClanService {
    @Autowired
    private ClanRepository clanDB;
    
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
}
