package com.itu.evaluation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.PaymentEntryReference;
import com.itu.evaluation.model.PurchaseInvoice;
import com.itu.evaluation.model.PurchaseInvoiceItem;
import com.itu.evaluation.model.Utilisateur;
import com.itu.evaluation.utils.ResponseUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountingService {
    private final RestTemplate restTemplate;
    private final HttpSession session;
    private final ApiService apiService;

    @Autowired
    public AccountingService(HttpSession session, ApiService apiService, RestTemplate restTemplate) {
        this.session = session;
        this.apiService = apiService;
        this.restTemplate = restTemplate;
    }

    public ResponseUtil getAllPurchaseInvoice() {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            List<PurchaseInvoice> purchaseInvoices = this.apiService.fetch("Purchase Invoice", PurchaseInvoice.class);

            Map<String, Object> data = new HashMap<>();
            data.put("listPurchaseInvoice", purchaseInvoices);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Purchase Invoice!");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }

    public ResponseUtil getItemPurchaseInvoice(String purchaseInvoiceName) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            PurchaseInvoice purchaseInvoice = this.apiService.fetchByName("Purchase Invoice", PurchaseInvoice.class, purchaseInvoiceName);
            List<PurchaseInvoiceItem> purchaseInvoiceItems = purchaseInvoice.getItems();
            List<PaymentEntryReference> paymentEntryReferences = getPayments(Constante.BASE_API_METHOD+"/erpnext.accounts.api.listPayment",purchaseInvoiceName);

            Map<String, Object> data = new HashMap<>();
            data.put("listPurchaseInvoiceItem",purchaseInvoiceItems);
            data.put("listPaymentEntryReference",paymentEntryReferences);
            data.put("purchaseInvoiceName",purchaseInvoiceName);
            data.put("purchaseInvoice",purchaseInvoice);

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setData(null);
            responseUtil.setError("Erreur lors de la recuperation des Item pour Purchase Invoice: "+purchaseInvoiceName+" !");
            responseUtil.setDetailError(e.getMessage());
        }

        return responseUtil;
    }

    public List<PaymentEntryReference> getPayments(String baseUrl, String purchaseInvoiceName) {
        List<PaymentEntryReference> listPayments = new ArrayList<>();

        try {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            String token = utilisateur.getToken();

            // Ajouter le token dans l'entête (headers)
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // Construire l'URL avec le paramètre GET
            String encodedInvoiceName = URLEncoder.encode(purchaseInvoiceName, StandardCharsets.UTF_8.toString());
            String urlWithParam = baseUrl + "?purchase_invoice_name=" + encodedInvoiceName;

            System.out.println("Url : "+urlWithParam);

            // Appeler l'API avec l'URL modifiée
            ResponseEntity<Map> response = restTemplate.exchange(
                    urlWithParam,
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();

                if (body != null && body.containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) body.get("message");

                    if (message.containsKey("listPayment")) {
                        List<Map<String, Object>> data = (List<Map<String, Object>>) message.get("listPayment");

                        ObjectMapper mapper = new ObjectMapper();

                        for (Map<String, Object> item : data) {
                            // Convertir chaque map vers un objet PaymentEntryReference
                            PaymentEntryReference payment = mapper.convertValue(item, PaymentEntryReference.class);
                            listPayments.add(payment);
                        }

                        System.out.println("Total Payment : "+listPayments.size());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listPayments;
    }

    public ResponseUtil payInvoice(String baseUrl ,String purchaseInvoiceName, String postingDate, double paidAmount, String modeOfPayment) {
        ResponseUtil responseUtil = new ResponseUtil();

        try {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            String token = utilisateur.getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payment = new HashMap<>();
            payment.put("doctype", "Payment Entry");
            payment.put("payment_type", "Pay");
            payment.put("party_type", "Supplier");

            PurchaseInvoice invoice = this.apiService.fetchByName("Purchase Invoice", PurchaseInvoice.class, purchaseInvoiceName);
            payment.put("party", invoice.getSupplier());
            payment.put("posting_date", postingDate);
            payment.put("paid_amount", paidAmount);
            payment.put("received_amount", paidAmount);
            payment.put("mode_of_payment", modeOfPayment);
            payment.put("reference_doctype", "Purchase Invoice");
            payment.put("reference_name", purchaseInvoiceName);

            payment.put("source_exchange_rate", 1.0);

            payment.put("paid_from", "Cash - FKD");
            payment.put("paid_from_account_currency", "USD");
            payment.put("paid_to", "Creditors - FKD");
            payment.put("paid_to_account_currency", "USD");

            List<Map<String, Object>> references = new ArrayList<>();
            Map<String, Object> reference = new HashMap<>();
            reference.put("reference_doctype", "Purchase Invoice");
            reference.put("reference_name", purchaseInvoiceName);
            reference.put("total_amount", invoice.getGrand_total());
            reference.put("outstanding_amount", invoice.getOutstanding_amount());
            reference.put("allocated_amount", paidAmount);
            references.add(reference);

            payment.put("references", references);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(payment);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                responseUtil.setStatus("error");
                responseUtil.setError("Erreur lors de payment!");
                responseUtil.setDetailError(response.getBody());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("message", "Paiement effectue");

            responseUtil.setStatus("success");
            responseUtil.setData(data);
            responseUtil.setError(null);
            responseUtil.setDetailError(null);
        } catch (Exception e) {
            responseUtil.setStatus("error");
            responseUtil.setError("Erreur lors de payment!");
            responseUtil.setDetailError(e.getMessage());
            e.printStackTrace();
        }

        return responseUtil;
    }
}
