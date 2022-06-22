package com.camping_rental.server.domain.region.enums;

public enum RegionDeletedFlagEnum {
    EXIST(false),
    DELETED(true)
    ;

    private boolean value;

    RegionDeletedFlagEnum(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
