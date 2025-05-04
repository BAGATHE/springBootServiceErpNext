package com.itu.evaluation.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuotationUpdateItem {
    private String item_code;
    private String description;
    private double qty;
    private double rate;
}
