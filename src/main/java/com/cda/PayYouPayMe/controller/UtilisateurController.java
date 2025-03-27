package com.cda.PayYouPayMe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.service.UtilisateurService;

@Controller
@RequestMapping("/me/user")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/")
    public String getCurrentUser(Model model) {
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        model.addAttribute("userToDisplay", utilisateur);
        return "affichageUser";
    }
//l'endpoint permettant de sauvegarder un utilisateur
   
    @PostMapping("/saveutilisateur")
    public String saveUtilisateur(Model model, @ModelAttribute Utilisateur userToSave) {
        try {
            utilisateurService.createUser(userToSave);
            model.addAttribute("userToDisplay", userToSave);
            return "affichageUser";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errorPage"; // Redirige vers une page d'erreur
        }
    }
 //l'endpoint permettant de suspendre un utilisateur
    @PostMapping("/suspendrecompte")
    public String supendreUtilisateur(Model model) {
        utilisateurService.suspendreCompte();
        return "home";
    }
}
