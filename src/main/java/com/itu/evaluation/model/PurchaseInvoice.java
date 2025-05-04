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
public class PurchaseInvoice {
    private String name;
    private String supplier;
    private String supplier_name;
    private String company;
    private String posting_date;
    private String due_date;
    private String buying_price_list;
    private String price_list_currency;
    private Double total;
    private Double total_qty;
    private Double grand_total;
    private Double paid_amount;
    private Double outstanding_amount;
    private String status;
    private List<PurchaseInvoiceItem> items;
    private List<PaymentEntryReference> payments;
}
