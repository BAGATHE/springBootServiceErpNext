package com.itu.evaluation.service;

import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.Supplier;
import com.itu.evaluation.model.Utilisateur;
import com.itu.evaluation.utils.FrappeResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    private final HttpSession session;
    private final RestTemplate restTemplate;

    public SupplierService(HttpSession session, RestTemplate restTemplate) {
        this.session = session;
        this.restTemplate = restTemplate;
    }




    public List<Supplier> findAll() {
        String url = Constante.suppliers;
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        String token = utilisateur.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<FrappeResponse<Supplier>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<FrappeResponse<Supplier>>() {}
        );

        return response.getBody() != null ? response.getBody().getData() : new ArrayList<>();
    }
}