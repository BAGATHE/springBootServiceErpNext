package com.itu.evaluation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.*;
import com.itu.evaluation.utils.FrappeResponse;
import com.itu.evaluation.utils.ResponseUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierService {
    private final HttpSession session;
    private final RestTemplate restTemplate;
    private final ApiService apiService;

    public SupplierService(HttpSession session, RestTemplate restTemplate,ApiService apiService) {
        this.session = session;
        this.restTemplate = restTemplate;
        this.apiService = apiService;
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





    public ResponseUtil getAllSupplierQuotation(String supplierName) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            String[][] criteria = {{"supplier", "=", supplierName}};
            List<SupplierQuotation> supplierQuotations = this.apiService.fetchCriteria("Supplier Quotation", SupplierQuotation.class, criteria);

            Map<String, Object> data = new HashMap<>();
            data.put("listSupplierQuotation", supplierQuotations);
            data.put("supplierName", supplierName);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Supplier Quotation pour Supplier: "+supplierName+"!");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }


    public ResponseUtil getItemSupplierQuotation(String supplierName,String supplierQuotationName) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            SupplierQuotation supplierQuotation = this.apiService.fetchByName("Supplier Quotation", SupplierQuotation.class, supplierQuotationName);
            List<SupplierQuotationItem> supplierQuotationItems = supplierQuotation.items;

            Map<String, Object> data = new HashMap<>();
            data.put("listSupplierQuotationItem", supplierQuotationItems);
            data.put("supplierQuotation", supplierQuotation);
            data.put("supplierName", supplierName);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Item pour Supplier Quotation: "+supplierQuotationName+"(Supplier = "+supplierName+")!");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }

    public ResponseUtil updateSupplierQuotationItem(String baseUrl, String supplierQuotationName, List<SupplierQuotationItem> updatedItems) {
        ResponseUtil responseUtil = new ResponseUtil();
        try {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            String token = utilisateur.getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construction du payload
            List<Map<String, Object>> itemsPayload = new ArrayList<>();

            for (SupplierQuotationItem item : updatedItems) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", item.getName());
                itemData.put("item_code", item.getItem_code());
                itemData.put("qty", item.getQty());
                itemData.put("rate", item.getRate());
                itemData.put("warehouse", item.getWarehouse());
                itemsPayload.add(itemData);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("items", itemsPayload);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "/" + supplierQuotationName,
                    HttpMethod.PUT,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                responseUtil.setStatus("success");
                responseUtil.setData(response.getBody());
            } else {
                responseUtil.setStatus("error");
                responseUtil.setError("Update failed");
                responseUtil.setDetailError("Status code: " + response.getStatusCode());
            }

        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setError("Exception occurred during update");
            responseUtil.setDetailError(e.getMessage());
            e.printStackTrace();
        }

        return responseUtil;
    }


    public ResponseUtil getAllPurchaseOrder(String supplierName) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            String[][] criteria = {{"supplier", "=", supplierName}};
            List<PurchaseOrder> purchaseOrders = this.apiService.fetchCriteria("Purchase Order", PurchaseOrder.class, criteria);

            Map<String, Object> data = new HashMap<>();
            data.put("listPurchaseOrder", purchaseOrders);
            data.put("supplierName", supplierName);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Purchase Order pour Supplier: "+supplierName+"!");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }


    public ResponseUtil getItemPurchaseOrder(String supplierName,String purchaseOrderName) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            PurchaseOrder purchaseOrder = this.apiService.fetchByName("Purchase Order", PurchaseOrder.class, purchaseOrderName);
            List<PurchaseOrderItem> purchaseOrderItems = purchaseOrder.getItems();

            Map<String, Object> data = new HashMap<>();
            data.put("listPurchaseOrderItem", purchaseOrderItems);
            data.put("supplierName", supplierName);
            data.put("purchaseOrder", purchaseOrder);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Item pour Purchase Order: "+purchaseOrderName+"(Supplier = "+supplierName+")!");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }

    public List<PurchaseOrder> getPurchaseOrderWithStatus(String baseUrl) {
        List<PurchaseOrder> listOrders = new ArrayList<>();

        try {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            String token = utilisateur.getToken();

            // Ajouter le token dans l'entête
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            System.out.println("URL : " + baseUrl);

            // Appel de l'API
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();

                if (body != null && body.containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) body.get("message");

                    // Change ici selon la vraie clé de ta réponse JSON pour les commandes d'achat
                    if (message.containsKey("listPurchaseOrder")) {
                        List<Map<String, Object>> data = (List<Map<String, Object>>) message.get("listPurchaseOrder");

                        ObjectMapper mapper = new ObjectMapper();

                        for (Map<String, Object> item : data) {
                            PurchaseOrder order = mapper.convertValue(item, PurchaseOrder.class);
                            listOrders.add(order);
                        }

                        System.out.println("Total Purchase Orders : " + listOrders.size());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listOrders;
    }

}