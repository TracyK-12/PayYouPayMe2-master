package com.cda.PayYouPayMe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cda.PayYouPayMe.model.Transaction;
import com.cda.PayYouPayMe.model.Utilisateur;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

    List<Transaction> findBySender(Utilisateur sender);
    List<Transaction> findByReciever(Utilisateur receiver);
    
	 @Query("SELECT t FROM Transaction t WHERE t.sender = :user OR t.reciever = :user ORDER BY t.date DESC")
	    List<Transaction> findAllByUser(@Param("user") Utilisateur user);
	 
	@Query("SELECT t FROM Transaction t JOIN t.sender s JOIN t.reciever r WHERE s.login = :login OR r.login = :login ORDER BY t.date DESC")
    List<Transaction> findAllByUserLogin(@Param("login") String login);
}
