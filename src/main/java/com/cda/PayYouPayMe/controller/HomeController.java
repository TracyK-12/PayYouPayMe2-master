package com.cda.PayYouPayMe.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.service.UtilisateurService;

@Controller
public class HomeController {

    private final PasswordEncoder passwordEncoder;
    private final UtilisateurService utilisateurService;

    public HomeController(PasswordEncoder passwordEncoder, UtilisateurService utilisateurService) {
        this.passwordEncoder = passwordEncoder;
        this.utilisateurService = utilisateurService;
    }

    // Page d'accueil, sans aucune restriction
    @GetMapping("/")
    public String home() {
        return "home"; // Affiche la page home.html
    }

    // Page de connexion
    @GetMapping("/login")
    public String login() {
        return "login"; // Affiche la page de connexion
    }

    // Page d'inscription
    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Affiche la page d'inscription
    }

    // Méthode pour gérer l'inscription de l'utilisateur
    @PostMapping("/inscription")
    public String inscription(Model model, @RequestParam String userName, @RequestParam String password) {
        // Vérifier si l'utilisateur existe déjà
        if (utilisateurService.getUserByUserName(userName) != null) {
            model.addAttribute("errorMessage", "Ce nom d'utilisateur existe déjà !");
            return "signup"; // Renvoie à la page d'inscription avec un message d'erreur
        }

        // Créer un nouvel utilisateur
        Utilisateur utilisateurToSave = new Utilisateur();
        utilisateurToSave.setUsername(userName);
        utilisateurToSave.setPassword(passwordEncoder.encode(password)); // Encode le mot de passe
        utilisateurToSave.setActif(true);
        utilisateurToSave.setBalance(0f);
        utilisateurToSave.setRole("USER");
        utilisateurToSave.setConfirmer(false); // Peut-être une gestion de confirmation de compte (en fonction de ta logique)

        // Enregistrer l'utilisateur dans la base de données
        utilisateurService.createUser(utilisateurToSave);

        model.addAttribute("successMessage", "Compte créé avec succès !");
        return "signup"; // Redirige vers la page login après inscription réussie
    }
}
