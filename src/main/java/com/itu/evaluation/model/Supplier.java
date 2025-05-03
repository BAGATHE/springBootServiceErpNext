package com.itu.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Supplier {
    private String name;
    private String supplier_name;
    private String supplier_group;
    private String country;
}
