package com.itu.evaluation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {
    @GetMapping("/dashboard")
    public String acceuilAdmin() {
        return "admin/dashboard";
    }
}