package com.itu.evaluation.controller;

import com.itu.evaluation.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
