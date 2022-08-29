package com.camping_rental.server.domain.product.enums;

public enum ProductPackageYnEnum {
    Y("y"),
    N("n");

    private String value;

    ProductPackageYnEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
