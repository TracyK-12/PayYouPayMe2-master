package com.cda.PayYouPayMe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cda.PayYouPayMe.service.MessageService;
import com.cda.PayYouPayMe.service.TransactionService;
import com.cda.PayYouPayMe.service.UtilisateurService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	private final MessageService messageService;
	private final TransactionService transactionService;
	private final UtilisateurService utilisateurService;
	
	public AdminController(MessageService messageService,
			TransactionService transactionService,
			UtilisateurService utilisateurService) {
		this.messageService = messageService;
		this.transactionService = transactionService;
		this.utilisateurService = utilisateurService;
	}
	
	
	
	@GetMapping("/")
	public String getHello() {
		return "hello";
	}
	
	@GetMapping("/alldatas")
	public String getAllDatas(Model model) {
        logger.info("Connect to allDatas!");

		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
	@PostMapping("/reponse")
	public String repondreMessage(Model model,  @RequestParam int id,
			@RequestParam String content) {
		messageService.repondreMessage(id, content);
		
		
		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
	@PostMapping("/desactiveruser")
	public String desactiverUser(Model model, @RequestParam int id) {
		utilisateurService.suspendreCompte(id);
		
		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
	@PostMapping("/confirmeruser")
	public String confirmerUser(Model model, @RequestParam int id) {
		utilisateurService.confirmerUser(id);
		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
	@PostMapping("/validertransaction")
	public String validerTransaction(Model model, @RequestParam int id) {
		transactionService.validerTransaction(id);
		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
	@PostMapping("/rejetertransaction")
	public String rejeterTransaction(Model model, @RequestParam int id) {
		transactionService.rejetTransaction(id);
		model.addAttribute("messages", messageService.getAllMessages());
		model.addAttribute("transactions", transactionService.getAllTransactions());
		model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
		return "alldatas";
	}
	
}
