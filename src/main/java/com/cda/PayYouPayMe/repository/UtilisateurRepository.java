package com.cda.PayYouPayMe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cda.PayYouPayMe.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer>{

    Optional<Utilisateur> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Utilisateur> findByLogin(String login);
   

}
