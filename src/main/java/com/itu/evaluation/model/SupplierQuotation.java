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
public class SupplierQuotation {
    public String name;
    public String supplier;
    public String currency;
    public Double conversion_rate;
    public Double total;
    public Double grand_total;
    public String buying_price_list;
    public String price_list_currency;
    public Double total_taxes_and_charges;
    public String company;
    public String status;
    public String transaction_date;
    public Integer docstatus;
    public String title;
    public List<SupplierQuotationItem> items;
}
