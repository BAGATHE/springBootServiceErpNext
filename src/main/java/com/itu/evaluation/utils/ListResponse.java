package com.itu.evaluation.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponse<T>{
    private List<T> data;
}
