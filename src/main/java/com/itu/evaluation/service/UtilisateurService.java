package com.itu.evaluation.service;

import com.itu.evaluation.model.Utilisateur;
import com.itu.evaluation.utils.ResponseUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilisateurService {
    private final HttpSession session;

    public UtilisateurService(HttpSession session) {
        this.session = session;
    }

    public ResponseUtil login(String baseUrl, String email, String mdp) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Corps de la requête (body JSON)
            Map<String, Object> body = new HashMap<>();
            body.put("usr", email);
            body.put("pwd", mdp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Envoyer la requête POST
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // Remplir le ResponseUtil en fonction de la réponse
            if (response.getStatusCode() == HttpStatus.OK) {
                responseUtil.setStatus("success");

                // Récupérer les données retournées
                Map<String, Object> data = response.getBody();
                if (data == null) {
                    data = new HashMap<>();
                }

                // Récupérer le Cookie "sid" (le token de session)
                HttpHeaders responseHeaders = response.getHeaders();
                List<String> setCookie = responseHeaders.get(HttpHeaders.SET_COOKIE);

                if (setCookie != null) {
                    for (String cookie : setCookie) {
                        if (cookie.startsWith("sid=")) {
                            String sid = cookie.split(";")[0].split("=")[1];
                            data.put("token", sid); // Ajouter le token dans les données
                            break;
                        }
                    }
                }

                responseUtil.setData(data);
                responseUtil.setError(null);
                responseUtil.setDetailError(null);
            } else {
                responseUtil.setStatus("fail");
                responseUtil.setError("Erreur d'authentification");
                responseUtil.setDetailError("Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setError("Exception");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }


    public List<String> getCustomers(String baseUrl) {
        List<String> customers = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            String token = utilisateur.getToken();

            // Ajouter le token dans l'entête (headers)
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // Appeler l'API avec headers
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body != null && body.containsKey("data")) {
                    List<Map<String, Object>> data = (List<Map<String, Object>>) body.get("data");
                    for (Map<String, Object> customer : data) {
                        String name = (String) customer.get("name");
                        customers.add(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
