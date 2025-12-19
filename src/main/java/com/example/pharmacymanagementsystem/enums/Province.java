package com.example.pharmacymanagementsystem.enums;

public enum Province {
    KIGALI("01", "Kigali City"),
    SOUTHERN("02", "Southern Province"),
    WESTERN("03", "Western Province"),
    NORTHERN("04", "Northern Province"),
    EASTERN("05", "Eastern Province");

    private final String code;
    private final String name;

    Province(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}