package com.camping_rental.server.domain.user.enums;


public enum UserLoginTypeEnum {
    LOCAL("local"),
    SOCIAL("social");

    private String value;

    UserLoginTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
