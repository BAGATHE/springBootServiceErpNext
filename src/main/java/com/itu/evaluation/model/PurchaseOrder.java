package com.itu.evaluation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrder {
    private String name;
    private String supplier;
    private String supplier_name;
    private String transaction_date;
    private String schedule_date;
    private String company;
    private String currency;
    private Double total_qty;
    private Double total;
    private Double grand_total;
    private String status;
    public String status_payment;
    public String status_reception;
    private String ref_sq;
    private List<PurchaseOrderItem> items;
}