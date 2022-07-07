package com.camping_rental.server.domain.product.enums;

public enum ProductDeletedFlagEnum {
    EXIST(false),
    DELETED(true);

    private boolean value;

    ProductDeletedFlagEnum(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
