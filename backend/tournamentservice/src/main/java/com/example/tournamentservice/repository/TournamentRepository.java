package com.example.tournamentservice.repository;

import com.example.tournamentservice.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TournamentRepository extends JpaRepository<T, Long> {

    List<Tournament> findByStatus(String status);
    List<Tournament> findByStatusAndName(String Status, String name);

    
    //Searching of tournament dynamically
    List<Tournament> findByCriteria();


    
}