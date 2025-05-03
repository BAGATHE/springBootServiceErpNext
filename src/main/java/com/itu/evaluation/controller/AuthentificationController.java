package com.itu.evaluation.controller;

import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.Utilisateur;
import com.itu.evaluation.service.UtilisateurService;
import com.itu.evaluation.utils.ResponseUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/authentification")
public class AuthentificationController {

    private final UtilisateurService utilisateurService;
    private final String baseUrl = Constante.baseUrl+"/method/login";

    @Autowired
    public AuthentificationController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String mdp,
                        HttpSession session, RedirectAttributes redirectAttributes) {

        ResponseUtil response = utilisateurService.login(baseUrl, email, mdp);

        if ("success".equals(response.getStatus())) {
            // Récupérer les données retournées
            Map<String, Object> data = response.getData();
            String fullName = (String) data.get("full_name");
            String token = (String) data.get("token");

            if (fullName != null && token != null) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setNom(fullName);
                utilisateur.setToken(token);

                System.out.println("Nom : "+utilisateur.getNom());
                System.out.println("Token : "+utilisateur.getToken());

                // Mettre l'utilisateur dans la session
                session.setAttribute("utilisateur", utilisateur);

                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Données d'authentification invalides");
                return "redirect:/authentification/login";
            }
        } else {
            redirectAttributes.addFlashAttribute("erreur", response.getError() != null ? response.getError() : "Erreur de connexion");
            return "redirect:/authentification/login";
        }
    }


    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}