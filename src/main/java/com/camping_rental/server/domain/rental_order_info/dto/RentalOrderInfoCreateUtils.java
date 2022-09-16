package com.camping_rental.server.domain.rental_order_info.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class RentalOrderInfoCreateUtils {
    public static void checkServiceAgreementYnValid(String serviceAgreementYn) {
        if (StringUtils.isBlank(serviceAgreementYn)) {
            throw new NotMatchedFormatException("잘못된 접근 방식 입니다.");
        }

        if (
                StringUtils.isBlank(serviceAgreementYn)
                        || !serviceAgreementYn.equals("y")
        ) {
            throw new NotMatchedFormatException("개인정보수집 및 이용에 동의 해주세요.");
        }
    }

    public static void checkOrdererValid(String orderer) {
        if (
                StringUtils.isBlank(orderer)
                        || !DataFormatUtils.isPassNameFormatValid(orderer)
        ) {
            throw new NotMatchedFormatException("주문자 정보를 확인할 수 없습니다. 같은 문제가 지속적으로 발생한다면 관리자에 문의해 주세요.");
        }
    }

    public static void checkOrdererPhoneNumberValid(String ordererPhoneNumber) {
        if (
                StringUtils.isBlank(ordererPhoneNumber)
                        || !DataFormatUtils.isPassPhoneNumberFormatValid(ordererPhoneNumber)
        ) {
            throw new NotMatchedFormatException("주문자 정보를 확인할 수 없습니다. 같은 문제가 지속적으로 발생한다면 관리자에 문의해 주세요.");
        }
    }

    public static void checkPickupPlaceValid(String pickupPlace) {
        if (StringUtils.isBlank(pickupPlace)) {
            throw new NotMatchedFormatException("픽업 | 반납 장소를 선택해 주세요.");
        }
    }

    public static void checkReturnPlaceValid(String returnPlace) {
        if (StringUtils.isBlank(returnPlace)) {
            throw new NotMatchedFormatException("픽업 | 반납 장소를 선택해 주세요.");
        }
    }

    public static void checkPickupDateValid(LocalDateTime pickupDate){
        if (pickupDate == null) {
            throw new NotMatchedFormatException("픽업 날짜를 선택해 주세요.");
        }
    }

    public static void checkReturnDateValid(LocalDateTime returnDate){
        if (returnDate == null) {
            throw new NotMatchedFormatException("반납 날짜를 선택해 주세요.");
        }
    }

    public static void checkPickupTimeValid(String pickupTime){
        if (StringUtils.isBlank(pickupTime)) {
            throw new NotMatchedFormatException("픽업 시간을 선택해 주세요.");
        }
    }

    public static void checkReturnTimeValid(String returnTime){
        if (StringUtils.isBlank(returnTime)) {
            throw new NotMatchedFormatException("반납 시간을 선택해 주세요.");
        }
    }

    public static void checkBorrowerValid(String borrower) {
        if(StringUtils.isBlank(borrower) || !DataFormatUtils.isPassNameFormatValid(borrower)){
            throw new NotMatchedFormatException("대여자 이름을 정확히 입력해 주세요.");
        }
    }

    public static void checkBorrowerPhoneNumberValid(String borrowerPhoneNumber) {
        if(StringUtils.isBlank(borrowerPhoneNumber) || !DataFormatUtils.isPassPhoneNumberFormatValid(borrowerPhoneNumber)){
            throw new NotMatchedFormatException("대여자 연락처를 정확히 입력해 주세요.");
        }
    }

    public static void checkBorrowerPhoneNumberValidationCodeValid(String borrowerPhoneNumberValidationCode) {
        if(StringUtils.isBlank(borrowerPhoneNumberValidationCode) || !DataFormatUtils.isPassPhoneValidationCodeValid(borrowerPhoneNumberValidationCode)){
            throw new NotMatchedFormatException("인증번호를 정확히 입력해 주세요.");
        }
    }

    public static void checkPhoneValidationValid(HttpServletRequest request, String borrowerPhoneNumber, String borrowerPhoneNumberValidationCode) {
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie == null) {
            throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
        }

        phoneValidationToken = phoneValidationCookie.getValue();

        String jwtSecret = borrowerPhoneNumber + borrowerPhoneNumberValidationCode + ValidationTokenUtils.getJwtEmailValidationSecret();
        CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");
    }
}
