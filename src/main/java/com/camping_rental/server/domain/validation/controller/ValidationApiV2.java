package com.camping_rental.server.domain.validation.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.validation.service.ValidationBusinessServiceV2;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/validations")
@RequiredArgsConstructor
public class ValidationApiV2 {
    private final ValidationBusinessServiceV2 validationBusinessService;

    @PostMapping("/phone/validation-code/action:send")
    public ResponseEntity<?> sendPhoneValidationCode(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Map<String, Object> params,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String validationType = null;
        String phoneNumber = null;

        try{
            validationType = params.get("validationType").toString();
            phoneNumber = body.get("phoneNumber").toString();
        } catch (NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 방식입니다.");
       }


        /*
        불필요한 중복 요청을 막기위해 10초 마다 해당 요청을 실행 할 수 있음.
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie != null) {
            phoneValidationToken = phoneValidationCookie.getValue();
            DecodedJWT jwt = JWT.decode(phoneValidationToken);

            long diffTime = CustomDateUtils.getCurrentTimeMillis() - jwt.getIssuedAt().getTime();

            if(diffTime < 10000){
                throw new NotMatchedFormatException("요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.");
            }
        }

        /*
        로직 실행
         */
        validationBusinessService.sendPhoneValidationCode(response, validationType, phoneNumber);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/email/validation-code/action:send")
    public ResponseEntity<?> sendEmailValidationCode(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Map<String, Object> params,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String validationType = null;
        String email = null;

        try{
            validationType = params.get("validationType").toString();
            email = body.get("email").toString();
        } catch (NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 방식 입니다.");
        }


        /*
        불필요한 중복 요청을 막기위해 5초 마다 해당 요청을 실행 할 수 있음.
         */
        Cookie emailValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_EMAIL_VALIDATION_TOKEN);

        String emailValidationToken = null;
        if (emailValidationCookie != null) {
            emailValidationToken = emailValidationCookie.getValue();
            DecodedJWT jwt = JWT.decode(emailValidationToken);

            long diffTime = CustomDateUtils.getCurrentTimeMillis() - jwt.getIssuedAt().getTime();

            if(diffTime < 10000){
                throw new NotMatchedFormatException("요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.");
            }
        }

        /*
        로직 실행
         */
        validationBusinessService.sendEmailValidationCode(response, validationType, email);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/phone/validation-code/action:check-validation")
    public ResponseEntity<?> checkPhoneValidationCode(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String phoneNumber = null;
        String validationCode = null;

        try{
            phoneNumber = body.get("phoneNumber").toString();
            validationCode = body.get("validationCode").toString();
        } catch (NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 방식입니다.");
        }

        /*
        로직 실행
         */
        validationBusinessService.checkPhoneValidation(request, phoneNumber, validationCode);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
