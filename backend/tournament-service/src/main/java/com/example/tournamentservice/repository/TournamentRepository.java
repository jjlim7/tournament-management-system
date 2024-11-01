package com.example.tournamentservice.repository;

import com.example.tournamentservice.entity.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    //@Autowired

    
    //List<Tournament> findByStatus(String status);
    //List<Tournament> findByStatusAndName(String Status, String name);

    
    //Searching of tournament dynamically
    //List<Tournament> findByCriteria();


    
}