package com.camping_rental.server.domain.rental_order_info.enums;

public enum RentalOrderInfoDeletedFlagEnum {
    EXIST(false),
    DELETED(true);

    private boolean value;

    RentalOrderInfoDeletedFlagEnum(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
