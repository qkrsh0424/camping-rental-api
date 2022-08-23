package com.camping_rental.server.domain.validation.strategy;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;

public enum EmailValidationName {
    ForModify("forModify")
    ;
    private String value;

    EmailValidationName(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static EmailValidationName fromString(String text) {
        for (EmailValidationName b : EmailValidationName.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }

        throw new NotMatchedFormatException("잘못된 접근 방식 입니다. 정상적인 방법을 이용해 주세요.");
    }
}
