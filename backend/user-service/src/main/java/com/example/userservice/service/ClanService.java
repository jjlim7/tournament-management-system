package com.example.userservice.service;

import com.example.userservice.entity.*;
import java.util.*;

public interface ClanService {
    // CRUD
    List<Clan> listAllClans();
    Clan addClan(Clan clan);
    Clan getClan(Long clanId);
    Clan updateClan(Long clanId, Clan newClan);
    void deleteClan(Long clanId);

    
} 
