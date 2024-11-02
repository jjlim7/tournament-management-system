package com.example.userservice.service;

import com.example.userservice.entity.*;
import com.example.userservice.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.userservice.repository.ClanUserRepository;

import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class ClanUserServiceImpl implements ClanUserService {
    @Autowired
    private ClanUserRepository clanUserDB;

    @Override
    @Transactional
    public List<ClanUser> listAllClanUsersFromClan(Long clanId) {
        return clanUserDB.findByClan_ClanId(clanId);
    }

    @Override
    @Transactional
    public List<ClanUser> listAllClanUsers() {
        return clanUserDB.findAll();
    }

    @Override
    @Transactional
    public ClanUser addClanUser(ClanUser clanUser) {
        /* ClanUser only exists if the clan and user are valid. 
         * So if clanId is invalid or if userId is invalid then ClanUser can't exist.
         * Can only throw UserNotFoundException or ClanNotFoundException
         */

        User user = clanUser.getUser();
        if(user == null ) {
            throw new UserNotFoundException();
        }

        Clan clan = clanUser.getClan();
        if(clan == null) {
            throw new ClanNotFoundException();   
        }

        return clanUserDB.save(clanUser);
    }

    @Override
    @Transactional
    public ClanUser getClanUser(Long clanUserId) {
        Optional<ClanUser> optionalClanUser = clanUserDB.findById(clanUserId);
        
        return optionalClanUser.orElse(null); // check this later
    }

    @Override
    @Transactional
    public ClanUser updateClanUser(Long clanUserId, ClanUser newClanUser) {
        // Check for Clan and User first
        // User newUser = newClanUser.getUser();
        // if(newUser == null ) {
        //     throw new UserNotFoundException();
        // }
        
        // Clan newClan = newClanUser.getClan();
        // if(newClan == null) {
        //     throw new ClanNotFoundException();   
        // }
        
        Optional<ClanUser> optionalClanUser = clanUserDB.findById(clanUserId);
        

        if(optionalClanUser.isPresent()) {
            ClanUser currentClanUser = optionalClanUser.get();

            currentClanUser.setClan(newClanUser.getClan());
            currentClanUser.setUser(newClanUser.getUser());
            currentClanUser.setIsClanLeader(newClanUser.getIsClanLeader());
            currentClanUser.setPosition(newClanUser.getPosition());

            return clanUserDB.save(currentClanUser);
        } else {
            return null;
        }


    }

    @Override
    @Transactional
        public void deleteClanUser(Long clanUserId) {
            clanUserDB.deleteById(clanUserId);
        }
}
