package com.cda.PayYouPayMe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    // Page d'accueil pour les administrateurs
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "adminDashboard"; // Page dédiée aux admins
    }

    // Page d'accueil pour les utilisateurs
    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "userDashboard"; // Page dédiée aux utilisateurs
    }
}
