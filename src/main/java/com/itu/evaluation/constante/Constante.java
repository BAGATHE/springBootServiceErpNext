package com.itu.evaluation.constante;

public class Constante {
    public static final String baseUrl = "http://erpnext.localhost:8000/api";
    public static final String suppliers = "http://erpnext.localhost:8000/api/resource/Supplier?fields=[\"name\",\"supplier_name\",\"supplier_group\",\"country\"]";
    public static final String SITE_HOST = "erpnext.localhost";
    public static final Integer PORT = 8000;
    public static final String BASE_URL = "http://"+SITE_HOST+":"+PORT;

    public static final String BASE_API = BASE_URL+"/api";

    public static final String BASE_API_METHOD = BASE_API + "/method";

    public static final String BASE_API_DOCTYPE = BASE_API + "/resource";
}
