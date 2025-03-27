package com.cda.PayYouPayMe.controller;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.service.UtilisateurService;
import com.cda.PayYouPayMe.service.AuthentificationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final AuthentificationService authentificationService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UtilisateurService utilisateurService, AuthentificationService authentificationService, BCryptPasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.authentificationService = authentificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signUp")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/signUp")
    public String handleSignUp(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setPassword(passwordEncoder.encode(password)); // Encoder le mot de passe
        utilisateur.setRole(role);
        utilisateurService.createUser(utilisateur); // Sauvegarde dans la base de données
        return "login"; // Redirige vers la page de connexion
    }
}
