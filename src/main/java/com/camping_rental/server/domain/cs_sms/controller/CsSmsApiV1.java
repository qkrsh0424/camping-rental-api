package com.camping_rental.server.domain.cs_sms.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.cs_sms.service.CsSmsBusinessService;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs-sms")
@RequiredArgsConstructor
public class CsSmsApiV1 {
    private final CsSmsBusinessService csSmsBusinessService;

    @PostMapping("/target:rentalOrderInfo/action:send-sms")
    @RequiredLogin
    public ResponseEntity<?> sendSms(
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String smsMessage = null;
        UUID rentalOrderInfoId = null;

        try{
            smsMessage = body.get("smsMessage").toString();
            rentalOrderInfoId = UUID.fromString(body.get("rentalOrderInfoId").toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        csSmsBusinessService.sendSmsForRentalOrderInfo(rentalOrderInfoId, smsMessage);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
