package com.camping_rental.server.domain.enums;

public enum DeletedFlagEnums {
    DELETED(true),
    EXIST(false);

    private boolean value;


    DeletedFlagEnums(boolean value) {
        this.value=value;
    }

    public boolean getValue() {
        return value;
    }
}
