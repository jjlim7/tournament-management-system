package com.security.auth.Security.repository;

import com.security.auth.Security.model.Token;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("select t from Token t inner join t.user p where p.email = :userEmail and t.loggedOut = false")
    List<Token> findAllTokensByUser(String userEmail);
    Optional<Token> findByToken(String token);

}
