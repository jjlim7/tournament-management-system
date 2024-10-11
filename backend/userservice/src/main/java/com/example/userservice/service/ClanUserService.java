package com.example.userservice.service;

import java.util.*;
import com.example.userservice.entity.*;

public interface ClanUserService {
    List<ClanUser> listAllClanUsers();
    ClanUser addClanUser(ClanUser clanUser);
    ClanUser getClanUser(Long clanUserId);
    ClanUser updateClanUser(Long clanUserId, ClanUser newClanUser);
    void deleteClanUser(Long clanUserId);

    // all methods are public abstract
    
} 
