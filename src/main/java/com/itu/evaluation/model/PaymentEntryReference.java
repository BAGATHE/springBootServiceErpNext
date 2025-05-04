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
public class PaymentEntryReference {
    private String reference_name;
    private String reference_doctype;
    private String due_date;
    private Double total_amount;
    private Double outstanding_amount;
    private Double allocated_amount;
    private String account;
    private String payment_entry_name;
    private String payment_type;
    private String posting_date;
    private String mode_of_payment;
    private String party_type;
    private String party;
    private Double paid_amount;
    private String paid_from_account_currency;
    private String paid_to_account_currency;
    private String reference_no;
    private String company;
    private String status;
}
