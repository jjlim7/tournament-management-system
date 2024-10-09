package com.example.userservice.service;

import com.example.userservice.entity.*;

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
    public List<ClanUser> listAllClanUsers() {
        return clanUserDB.findAll();
    }

    @Override
    @Transactional
    public ClanUser addClanUser(ClanUser clanUser) {
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
