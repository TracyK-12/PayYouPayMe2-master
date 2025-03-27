package com.cda.PayYouPayMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cda.PayYouPayMe.model.Transfert;

@Repository
public interface TransfertRepository extends JpaRepository<Transfert, Integer>{

}
