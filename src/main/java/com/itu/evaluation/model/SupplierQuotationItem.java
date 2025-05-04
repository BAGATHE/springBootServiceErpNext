package com.itu.evaluation.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierQuotationItem {
    public String name;
    public String parent;
    public Integer idx;
    public String item_code;
    public String item_name;
    public String description;
    public Double qty;
    public String uom;
    public Double conversion_factor;
    public Double rate;
    public Double discount_percentage;
    public Double discount_amount;
    public Double base_rate;
    public Double amount;
    public Double base_amount;
    public String warehouse;
    public String item_group;
    public String image;
}
