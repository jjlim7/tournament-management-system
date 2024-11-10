package com.example.userservice.service;

import java.util.*;
import com.example.userservice.entity.*;
import jakarta.transaction.Transactional;

public interface ClanUserService {
    List<ClanUser> listAllClanUsersFromClan(Long clanId);
    List<ClanUser> listAllClanUsers();
    ClanUser addClanUser(ClanUser clanUser);
    ClanUser getClanUser(Long clanUserId);
    ClanUser getClanUserByUserId(Long userId);
    ClanUser updateClanUser(Long clanUserId, ClanUser newClanUser);
    void deleteClanUser(Long clanUserId);

    // all methods are public abstract
    
} 
