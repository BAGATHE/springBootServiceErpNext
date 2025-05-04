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
public class PurchaseOrderItem {
    public String name;
    private String item_code;
    private String item_name;
    private String item_group;
    private String description;
    private Double qty;
    private String uom;
    private Double rate;
    private Double amount;
}
