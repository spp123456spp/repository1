package com.ydwf.ocr.demo;

public enum Company {


    中韩("123456");

    private String key;

    Company(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static void main(String[] args) {
        Company company1 = Company.中韩;
        System.out.println(company1.getKey());
    }


}
