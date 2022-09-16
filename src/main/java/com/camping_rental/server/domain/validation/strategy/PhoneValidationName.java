package com.camping_rental.server.domain.validation.strategy;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;

public enum PhoneValidationName {
    ForFindUsername("forFindUsername"),
    ForFindPassword("forFindPassword"),
    ForModify("forModify"),
    ForRentalOrder("forRentalOrder")
    ;
    private String value;

    PhoneValidationName(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static PhoneValidationName fromString(String text) {
        for (PhoneValidationName b : PhoneValidationName.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
//        return ForFindUsername;
        throw new NotMatchedFormatException("잘못된 접근 방식 입니다. 정상적인 방법을 이용해 주세요.");
    }
}
