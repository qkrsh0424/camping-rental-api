package com.camping_rental.server.utils;


import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;

import java.util.regex.Pattern;

public class DataFormatUtils {

    public static boolean isPassSignupUsername(String username) {
        boolean bool = Pattern.compile("^[a-z]([._a-z0-9]){4,19}$").matcher(username).find();

        return bool;
    }

    public static boolean isPassSignupEmail(String email){
        boolean bool = Pattern.compile("^([\\w._-])*[a-zA-Z0-9]+([\\w._-])*([a-zA-Z0-9])+([\\w._-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$").matcher(email).find();

        return bool;
    }

    public static boolean isPassSignupPassword(String password) {
        long num = Pattern.compile("[0-9]").matcher(password).results().count();
        long eng = Pattern.compile("[a-z]").matcher(password).results().count();
        long spe = Pattern.compile("[\\\\!@#$%^&*()\\-_=+\\[\\]{};:`\"',.<>/?|~]").matcher(password).results().count();
        long space = Pattern.compile("\\s").matcher(password).results().count();

        /*
        글자수 8 ~ 50
         */
        if (password.length() < 8 || password.length() > 50) {
            return false;
        }

        /*
        공백 체크
         */
        if (space > 0) {
            return false;
        }

        /*
        문자, 숫자, 특수문자 체크
         */
        if (num < 0 || eng < 0 || spe < 0) {
            return false;
        }

        return true;
    }

    public static boolean isPassSignupPhoneNumber(String phoneNumber) {
        boolean bool = Pattern.compile("^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$").matcher(phoneNumber).find();

        return bool;
    }

    public static void checkPhoneNumberFormat(String number) {
        boolean isPhoneNumberFormat = Pattern.compile("^01(?:0|1|[6-9])([0-9]{3,4})([0-9]{4})$").matcher(number).find();

        if (!isPhoneNumberFormat) {
            throw new NotMatchedFormatException("전화번호 형식이 올바르지 않습니다.");
        }
    }

    public static boolean isPhoneNumberFormatValid(String number) {
        boolean isPhoneNumberFormatValid = Pattern.compile("^01(?:0|1|[6-9])([0-9]{3,4})([0-9]{4})$").matcher(number).find();

        return isPhoneNumberFormatValid;
    }

    public static void checkEmailFormat(String email) {
//        boolean isEmailAddressFormat = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([0-9a-zA-Z]+\\.)+[0-9a-zA-Z]{2,8}$").matcher(email).find();
        boolean isEmailAddressFormat = Pattern.compile("^([\\w._-])*[a-zA-Z0-9]+([\\w._-])*([a-zA-Z0-9])+([\\w._-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$").matcher(email).find();
        if (!isEmailAddressFormat) {
            throw new NotMatchedFormatException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public static boolean isPassSignupNickname(String nickname) {
        if(nickname.length() < 2 || nickname.length() > 15){
            return false;
        }

        return true;
    }
}
