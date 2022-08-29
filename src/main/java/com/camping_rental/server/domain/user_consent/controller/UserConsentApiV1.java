package com.camping_rental.server.domain.user_consent.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.user_consent.service.UserConsentBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-consents")
@RequiredArgsConstructor
public class UserConsentApiV1 {
    private final UserConsentBusinessService userConsentBusinessService;

    @GetMapping("/my")
    @RequiredLogin
    public ResponseEntity<?> searchMy(){
        Message message = new Message();

        message.setData(userConsentBusinessService.searchMy());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:marketingPhoneYn")
    @RequiredLogin
    public ResponseEntity<?> changeMarketingPhoneYn(
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String marketingPhoneYn = null;

        try{
            marketingPhoneYn = body.get("marketingPhoneYn").toString();
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 입니다.");
        }

        userConsentBusinessService.changeMarketingPhoneYn(marketingPhoneYn);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:marketingEmailYn")
    @RequiredLogin
    public ResponseEntity<?> changeMarketingEmailYn(
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String marketingEmailYn = null;

        try{
            marketingEmailYn = body.get("marketingEmailYn").toString();
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 입니다.");
        }

        userConsentBusinessService.changeMarketingEmailYn(marketingEmailYn);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
