package com.itu.evaluation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.Utilisateur;
import com.itu.evaluation.utils.ListResponse;
import com.itu.evaluation.utils.SingleResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApiService {
    private final HttpSession session;
    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(HttpSession session) {
        this.session = session;
        this.restTemplate = new RestTemplate();
    }

    public String generateFields(String[] fields){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0 ; i < fields.length - 1 ; i++){
            builder.append('"');
            builder.append(fields[i]);
            builder.append('"');
            builder.append(',');
        }
        builder.append('"');
        builder.append(fields[fields.length - 1]);
        builder.append('"');
        builder.append("]");
        return builder.toString();
    }

    public String generateCriteria(String[][] criteria) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < criteria.length; i++) {
            String[] c = criteria[i];
            if (c.length != 3) {
                throw new Exception("Erreur filtre incomplete");
            }
            if (i > 0) {
                builder.append(",");
            }
            generateBraces(builder, c);
        }

        builder.append("]");
        return builder.toString();
    }

    private void generateBraces(StringBuilder builder, String[] c) {
        builder.append('[');
        builder.append('"');
        builder.append(c[0]);
        builder.append('"');
        builder.append(',');

        builder.append('"');
        builder.append(c[1]);
        builder.append('"');
        builder.append(',');

        builder.append('"');
        builder.append(c[2]);
        builder.append('"');
        builder.append(']');
    }

    public <T> T fetchOne(String doctypeName, Class<T> type)throws Exception{
        List<T> data = this.fetch(doctypeName, type);
        if(data.isEmpty()) return null;
        return data.get(0);
    }

    public <T> T fetchOneByCriteria(String doctypeName, Class<T> type, String[][] criteria)throws Exception{
        List<T> data = this.fetchCriteria(doctypeName, type, criteria);
        if(data.isEmpty()) return null;
        return data.get(0);
    }

    public <T> List<T> fetch(String doctypeName, Class<T> type)throws Exception{
        return this.fetchProjectionAndCriteria(doctypeName, type, new String[]{"*"}, new String[0][]);
    }

    public <T> List<T> fetchProjection(String doctypeName, Class<T> type, String[] fields)throws Exception{
        return this.fetchProjectionAndCriteria(doctypeName, type, fields, new String[0][]);
    }

    public <T> List<T> fetchCriteria(String doctypeName, Class<T> type, String[][] criteria)throws Exception{
        return this.fetchProjectionAndCriteria(doctypeName, type, new String[]{"*"}, criteria);
    }

    public <T> T fetchByName(String doctypeName, Class<T> type, String name) throws Exception {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        String token = utilisateur.getToken();

        if (token == null) {
            throw new Exception("Erreur authentification : Session non authentifiee (SID manquant)");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<SingleResponse<T>> typeRef = new ParameterizedTypeReference<>() {};
        System.out.println(Constante.BASE_API_DOCTYPE+ "/" + doctypeName + "/" + name);

        try {
            ResponseEntity<SingleResponse<T>> response = restTemplate.exchange(
                    Constante.BASE_API_DOCTYPE + "/" + doctypeName + "/" + name,
                    HttpMethod.GET,
                    entity,
                    typeRef
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object raw = response.getBody().getData();
                ObjectMapper mapper = new ObjectMapper();
                return mapper.convertValue(raw, type);
            } else {
                throw new Exception("Erreur HTTP: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new Exception("Erreur d appel API : " + e.getMessage(), e);
        }
    }


    public <T> List<T> fetchProjectionAndCriteria(String doctypeName, Class<T> type, String[] fields, String[][] filters) throws Exception {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        String token = utilisateur.getToken();

        if (token == null) {
            throw new Exception("Erreur authentification : Session non authentifiee (SID manquant)");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<ListResponse<T>> typeRef = new ParameterizedTypeReference<>() {};

        String projection = "&fields="+this.generateFields(fields);
        String criteria = "?filters="+this.generateCriteria(filters);
        System.out.println(Constante.BASE_API_DOCTYPE+"/"+doctypeName+criteria+projection);

        try {
            ResponseEntity<ListResponse<T>> response = restTemplate.exchange(
                    Constante.BASE_API_DOCTYPE + "/" + doctypeName + criteria + projection,
                    HttpMethod.GET,
                    entity,
                    typeRef
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<?> rawList = response.getBody().getData();
                ObjectMapper mapper = new ObjectMapper();
                List<T> typedList = new ArrayList<>();
                for (Object obj : rawList) {
                    typedList.add(mapper.convertValue(obj, type));
                }
                return typedList;
            } else {
                throw new Exception("Erreur HTTP: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new Exception("Erreur d appel API : " + e.getMessage(), e);
        }
    }
}
