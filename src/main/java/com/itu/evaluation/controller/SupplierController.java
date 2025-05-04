package com.itu.evaluation.controller;

import com.itu.evaluation.constante.Constante;
import com.itu.evaluation.model.*;
import com.itu.evaluation.service.SupplierService;
import com.itu.evaluation.utils.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
public class SupplierController {
public final SupplierService supplierService;
public SupplierController(SupplierService supplierService) {
    this.supplierService = supplierService;
}

@GetMapping("/suppliers")
    public String suppliers(Model model) {
    model.addAttribute("suppliers", supplierService.findAll());
    return "admin/supplier/list";
}

    @GetMapping("/supplier/{supplierName}/supplierQuotations")
    public String getAllSupplierQuotation(@PathVariable String supplierName, Model model) {
        ResponseUtil response = supplierService.getAllSupplierQuotation(supplierName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<SupplierQuotation> listSupplierQuotation = (List<SupplierQuotation>) data.get("listSupplierQuotation");

            model.addAttribute("listSupplierQuotation", listSupplierQuotation);
            model.addAttribute("supplierName", supplierName);
        } else {
            model.addAttribute("error", response.getError());
            model.addAttribute("errorDetail", response.getDetailError());
        }
        return "admin/supplier/listSupplierQuotation";
    }
    @GetMapping("/supplier/{supplierName}/supplierQuotations/{supplierQuotationName}/update")
    public String pageUpdateSupplierQuotation(@PathVariable String supplierName, @PathVariable String supplierQuotationName, Model model) {
        ResponseUtil response = supplierService.getItemSupplierQuotation(supplierName, supplierQuotationName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<SupplierQuotationItem> listsupplierQuotation = (List<SupplierQuotationItem>) data.get("listSupplierQuotationItem");
            SupplierQuotation supplierQuotation = (SupplierQuotation) data.get("supplierQuotation");

            model.addAttribute("listSupplierQuotationItem", listsupplierQuotation);
            model.addAttribute("supplierName", supplierName);
            model.addAttribute("supplierQuotation", supplierQuotation);
        } else {
            model.addAttribute("error", response.getError());
            model.addAttribute("errorDetail", response.getDetailError());
        }
        return "admin/supplier/updateSupplierQuotation";
    }

    @PostMapping("/supplier/{supplierName}/supplierQuotations/{supplierQuotationName}/update")
    public String updateSupplierQuotation(
            @PathVariable String supplierName,
            @PathVariable String supplierQuotationName,
            @ModelAttribute("items") SupplierQuotationItemListWrapper wrapper,
            Model model
    ) {
        String baseUrl = Constante.BASE_API_DOCTYPE+"/Supplier Quotation";

        if (wrapper.getItems() == null || wrapper.getItems().isEmpty()) {
            model.addAttribute("error", "Aucun item à mettre à jour.");
            return "redirect:/supplier/" + supplierName + "/supplierQuotations/" + supplierQuotationName + "/items";
        }

        ResponseUtil response = supplierService.updateSupplierQuotationItem(baseUrl, supplierQuotationName, wrapper.getItems());

        if ("success".equals(response.getStatus())) {
            model.addAttribute("success", "Mise à jour réussie !");
            System.out.println("Success!!!");
        } else {
            model.addAttribute("error", response.getError());
        }

        return "redirect:/supplier/" + supplierName + "/supplierQuotations/" + supplierQuotationName + "/items";
    }


    @GetMapping("/supplier/{supplierName}/supplierQuotations/{supplierQuotationName}/items")
    public String getItemSupplierQuotation(@PathVariable String supplierName, @PathVariable String supplierQuotationName, Model model) {
        ResponseUtil response = supplierService.getItemSupplierQuotation(supplierName, supplierQuotationName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<SupplierQuotationItem> listsupplierQuotation = (List<SupplierQuotationItem>) data.get("listSupplierQuotationItem");
            SupplierQuotation supplierQuotation = (SupplierQuotation) data.get("supplierQuotation");

            model.addAttribute("listSupplierQuotationItem", listsupplierQuotation);
            model.addAttribute("supplierName", supplierName);
            model.addAttribute("supplierQuotation", supplierQuotation);
        } else {
            model.addAttribute("error", response.getError());
            model.addAttribute("errorDetail", response.getDetailError());
        }
        return "admin/supplier/listSupplierQuotationItem";
    }

    @GetMapping("/supplier/{supplierName}/purchaseOrders")
    public String getAllPurchaseOrder(@PathVariable String supplierName, Model model) {
        ResponseUtil response = supplierService.getAllPurchaseOrder(supplierName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<PurchaseOrder> listPurchaseOrder = (List<PurchaseOrder>) data.get("listPurchaseOrder");

            model.addAttribute("listPurchaseOrder", listPurchaseOrder);
            model.addAttribute("supplierName", supplierName);
        } else {
            model.addAttribute("error", response.getError());
            model.addAttribute("errorDetail", response.getDetailError());
        }
        return "admin/supplier/listPurchaseOrder";
    }


    @GetMapping("/supplier/{supplierName}/purchaseOrders/{purchaseOrderName}/items")
    public String getItemPurchaseOrder(@PathVariable String supplierName, @PathVariable String purchaseOrderName, Model model) {
        ResponseUtil response = supplierService.getItemPurchaseOrder(supplierName, purchaseOrderName);

        if ("success".equals(response.getStatus())) {
            Map<String, Object> data = response.getData();
            List<PurchaseOrderItem> listPurchaseOrderItem = (List<PurchaseOrderItem>) data.get("listPurchaseOrderItem");
            PurchaseOrder purchaseOrder = (PurchaseOrder) data.get("purchaseOrder");

            model.addAttribute("listPurchaseOrderItem", listPurchaseOrderItem);
            model.addAttribute("purchaseOrder", purchaseOrder);
            model.addAttribute("supplierName", supplierName);
        } else {
            model.addAttribute("error", response.getError());
            model.addAttribute("errorDetail", response.getDetailError());
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

        return "admin/supplier/listPurchaseOrderItem";
    }

    @GetMapping("/supplier/{supplierName}/purchaseOrdersStatus")
    public String getAllPurchaseOrderWithStatus(@PathVariable String supplierName, Model model) {
        try{
            List<PurchaseOrder> listPurchaseOrder = supplierService.getPurchaseOrderWithStatus(Constante.BASE_API_METHOD+"/erpnext.accounts.api.get_purchase_orders");
            model.addAttribute("listPurchaseOrder", listPurchaseOrder);
            model.addAttribute("supplierName", supplierName);
        } catch (Exception e) {
            model.addAttribute("error", "Error lors de la recuperation des Purchase Order");
            model.addAttribute("errorDetail", e.getMessage());
        }

        return "admin/supplier/listPurchaseOrderStatus";
    }



}
