package com.camping_rental.server.domain.rental_order_product.enums;

public enum RentalOrderProductDeletedFlagEnum {
    EXIST(false),
    DELETED(true);

    private boolean value;

    RentalOrderProductDeletedFlagEnum(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
