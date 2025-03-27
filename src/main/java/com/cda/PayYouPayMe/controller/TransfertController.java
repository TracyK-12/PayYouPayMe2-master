package com.cda.PayYouPayMe.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cda.PayYouPayMe.model.Transfert;
import com.cda.PayYouPayMe.service.TransfertService;
import com.cda.PayYouPayMe.service.UtilisateurService;

@Controller
@RequestMapping("/transfert")
public class TransfertController {

	private final TransfertService transferService;
	private final UtilisateurService utilisateurService;

	public TransfertController(TransfertService transfertService,
			UtilisateurService utilisateurService) {
		this.transferService = transfertService;
		this.utilisateurService = utilisateurService;
	}
	
	
	@GetMapping("/")
	public String getTransfert(Model model) {
		model.addAttribute("utilisateur", utilisateurService.getCurrentUser());
		return "transfert";
	}
	
	@PostMapping("/createTransfert")
	public String createTransfert(Model model, @RequestParam String content, @RequestParam Float amount) {
		transferService.createTransfert(content, amount);
		
		model.addAttribute("utilisateur", utilisateurService.getCurrentUser());
		return "transfert";
	}
}
