package com.itu.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuotationUpdate {
    private String quotationName;
    private String supplierName;
    private List<QuotationUpdateItem> items;
}
