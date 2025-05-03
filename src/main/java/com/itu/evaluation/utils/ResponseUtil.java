package com.itu.evaluation.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUtil {
    private String status;
    private Map<String, Object> data;
    private String error;
    private String detailError;

    public ResponseUtil() {
        this.data = new HashMap<>();
    }
}