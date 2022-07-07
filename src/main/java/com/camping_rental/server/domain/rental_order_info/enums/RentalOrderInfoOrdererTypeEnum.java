package com.camping_rental.server.domain.rental_order_info.enums;

public enum RentalOrderInfoOrdererTypeEnum {
    MEMBER("member"),
    NON_MEMBER("nonMember");

    private String value;

    RentalOrderInfoOrdererTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
