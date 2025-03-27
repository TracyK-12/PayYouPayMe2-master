package com.cda.PayYouPayMe.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cda.PayYouPayMe.model.Transfert;
import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.repository.TransfertRepository;

@Service
public class TransfertService {

	private final TransfertRepository transfertRepository;
	private final UtilisateurService utilisateurService;
	
	public TransfertService(TransfertRepository transfertRepository,
			UtilisateurService utilisateurService) {
		this.transfertRepository = transfertRepository;
		this.utilisateurService = utilisateurService;
	}

	public List<Transfert> getAllTransferts() {
		return transfertRepository.findAll();
	}

	public void createTransfert(String content, Float amount) {
		Utilisateur utilisateur = utilisateurService.getCurrentUser();

		
		Transfert transfert = new Transfert();
		transfert.setAmount(amount);
		transfert.setDate(LocalDate.now());
		transfert.setMessageContent(content);
		transfert.setSender(utilisateur);
		
		if(amount<0 && utilisateur.getBalance() > Math.abs(amount)) {
			utilisateur.setBalance(utilisateur.getBalance() - Math.abs(amount));
		}
		else {
			utilisateur.setBalance(utilisateur.getBalance() + Math.abs(amount));
		}
		
		utilisateurService.updateUser(utilisateur);
		transfertRepository.save(transfert);
	}
}
