package com.itu.evaluation.controller;


import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.PaymentEntryReference;
import com.itu.evaluation.model.PurchaseInvoice;
import com.itu.evaluation.model.PurchaseInvoiceItem;
import com.itu.evaluation.service.AccountingService;
import com.itu.evaluation.service.ApiService;
import com.itu.evaluation.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class AccountingController {
    private final AccountingService accountingService;
    private final ApiService apiService;

    @Autowired
    public AccountingController(AccountingService accountingService, ApiService apiService) {
        this.accountingService = accountingService;
        this.apiService = apiService;
    }

    @GetMapping("/accounting/purchaseInvoices")
    public String getAllPurchaseInvoice(Model model) {
        ResponseUtil response = accountingService.getAllPurchaseInvoice();

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<PurchaseInvoice> listPurchaseInvoice = (List<PurchaseInvoice>) data.get("listPurchaseInvoice");

            model.addAttribute("listPurchaseInvoice",listPurchaseInvoice);
        } else {
            model.addAttribute("error",response.getError());
            model.addAttribute("errorDetail",response.getDetailError());
        }
        return "admin/accounting/listPurchaseInvoice";
    }

    @GetMapping("/accounting/purchaseInvoices/{purchaseInvoiceName}/items")
    public String getItemPurchaseInvoice(@PathVariable String purchaseInvoiceName, Model model) {
        ResponseUtil response = accountingService.getItemPurchaseInvoice(purchaseInvoiceName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<PurchaseInvoiceItem> listPurchaseInvoiceItem = (List<PurchaseInvoiceItem>) data.get("listPurchaseInvoiceItem");
            List<PaymentEntryReference> listPaymentEntryReference = (List<PaymentEntryReference>) data.get("listPaymentEntryReference");
            PurchaseInvoice purchaseInvoice = (PurchaseInvoice) data.get("purchaseInvoice");

            model.addAttribute("listPurchaseInvoiceItem",listPurchaseInvoiceItem);
            model.addAttribute("listPaymentEntryReference",listPaymentEntryReference);
            model.addAttribute("purchaseInvoiceName",purchaseInvoiceName);
            model.addAttribute("purchaseInvoice",purchaseInvoice);
        } else {
            model.addAttribute("error",response.getError());
            model.addAttribute("errorDetail",response.getDetailError());
        }

        // Récupération du message de succès
        if (model.containsAttribute("success")) {
            String success = (String) model.getAttribute("success");
            model.addAttribute("success",success);
        }
        // Récupération de l'erreur principale
        if (model.containsAttribute("error")) {
            String error = (String) model.getAttribute("error");
            model.addAttribute("error",error);
        }
        // Récupération du détail de l'erreur
        if (model.containsAttribute("errorDetail")) {
            String errorDetail = (String) model.getAttribute("errorDetail");
            model.addAttribute("errorDetail",errorDetail);
        }

        return "admin/accounting/listPurchaseInvoiceItem";
    }

    @GetMapping("/purchaseInvoices/{purchaseInvoiceName}/payment")
    public String pagePaymentPurchaseInvoice(@PathVariable String purchaseInvoiceName, Model model) {
        ResponseUtil response = accountingService.getItemPurchaseInvoice(purchaseInvoiceName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<PurchaseInvoiceItem> listPurchaseInvoiceItem = (List<PurchaseInvoiceItem>) data.get("listPurchaseInvoiceItem");
            List<PaymentEntryReference> listPaymentEntryReference = (List<PaymentEntryReference>) data.get("listPaymentEntryReference");
            PurchaseInvoice purchaseInvoice = (PurchaseInvoice) data.get("purchaseInvoice");

            model.addAttribute("listPurchaseInvoiceItem",listPurchaseInvoiceItem);
            model.addAttribute("listPaymentEntryReference",listPaymentEntryReference);
            model.addAttribute("purchaseInvoiceName",purchaseInvoiceName);
            model.addAttribute("purchaseInvoice",purchaseInvoice);
        } else {
            model.addAttribute("error",response.getError());
            model.addAttribute("errorDetail",response.getDetailError());
        }
        return "admin/accounting/payment";
    }

    @PostMapping("/purchaseInvoices/{purchaseInvoiceName}/payment")
    public String paymentPurchaseInvoice(@PathVariable String purchaseInvoiceName,
                                         @RequestParam("posting_date") String postingDate,
                                         @RequestParam("paid_amount") double paidAmount,
                                         @RequestParam("mode_of_payment") String modeOfPayment,
                                         Model model) {

        ResponseUtil response = accountingService.payInvoice(Constante.BASE_API_DOCTYPE + "/Payment Entry", purchaseInvoiceName, postingDate, paidAmount, modeOfPayment);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            String success = (String) data.get("message");

            System.out.println("success: " + success);
            model.addAttribute("success",success);
        } else {
            model.addAttribute("error",response.getError());
            model.addAttribute("errorDetail",response.getDetailError());
        }

        return "redirect:/admin/accounting/purchaseInvoices/"+ purchaseInvoiceName + "/items";
    }
}
