package com.camping_rental.server.domain.product.enums;

public enum ProductDisplayYnEnum {
    Y("y"),
    N("n");

    private String value;

    ProductDisplayYnEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
