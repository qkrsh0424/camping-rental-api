package com.camping_rental.server.domain.validation.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.validation.service.ValidationBusinessService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/validations")
@RequiredArgsConstructor
public class ValidationApiV1 {
    private final ValidationBusinessService validationBusinessService;

    @PostMapping("/phone/validation-code/action:send")
    public ResponseEntity<?> sendPhoneValidationCode(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String phoneNumber = null;

        try{
            phoneNumber = body.get("phoneNumber").toString();
        } catch (NullPointerException e){
            throw new NotMatchedFormatException("휴대폰 번호를 정확히 입력해 주세요.");
       }


        /*
        불필요한 중복 요청을 막기위해 10초 마다 해당 요청을 실행 할 수 있음.
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie != null) {
            phoneValidationToken = phoneValidationCookie.getValue();
            DecodedJWT jwt = JWT.decode(phoneValidationToken);
            long diffTime = CustomDateUtils.getCurrentTimeMillis() - jwt.getExpiresAt().getTime();

            if(diffTime < 10000){
                throw new NotMatchedFormatException("요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.");
            }
        }

        /*
        로직 실행
         */
        validationBusinessService.sendPhoneAuthCode(response, phoneNumber);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
