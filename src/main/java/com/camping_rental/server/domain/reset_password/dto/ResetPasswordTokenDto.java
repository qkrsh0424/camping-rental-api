package com.camping_rental.server.domain.reset_password.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ResetPasswordTokenDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String username;
        private String phoneNumber;
        private String phoneNumberValidationCode;

        public static void checkFormValid(HttpServletRequest request, Create dto){
            /*
            username 형식 체크
             */
            if(!DataFormatUtils.isPassSignupUsername(dto.getUsername())){
                throw new NotMatchedFormatException("입력하신 아이디 형식이 정확한지 확인하여 주세요.");
            }

            /*
            휴대전화 형식 체크
             */
            if (!DataFormatUtils.isPassSignupPhoneNumber(dto.getPhoneNumber())) {
                throw new NotMatchedFormatException("입력하신 휴대전화 형식이 정확한지 확인하여 주세요.");
            }

            /*
            휴대전화 인증번호 체크
             */
            Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

            String phoneValidationToken = null;
            if (phoneValidationCookie == null) {
                throw new NotMatchedFormatException("인증번호가 정확하지 않습니다.");
            }

            phoneValidationToken = phoneValidationCookie.getValue();

            String jwtSecret = dto.getPhoneNumber() + dto.getPhoneNumberValidationCode() + ValidationTokenUtils.getJwtEmailValidationSecret();
            CustomJwtUtils.parseJwt(jwtSecret, phoneValidationToken, "인증번호가 정확하지 않습니다.");
        }
    }
}
