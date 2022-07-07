package com.camping_rental.server.domain.product_image.enums;

public enum ProductImageDeletedFlagEnum {
    EXIST(false),
    DELETED(true);

    private boolean value;

    ProductImageDeletedFlagEnum(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
