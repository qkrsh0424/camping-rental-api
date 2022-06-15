package com.camping_rental.server.domain.user.enums;

public enum UserAllowedAccessCountEnum {
    DEFUALT(1);

    private int value;

    UserAllowedAccessCountEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
