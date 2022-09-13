package com.camping_rental.server.domain.cs_sms_log.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.cs_sms_log.service.CsSmsLogBusinessService;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs-sms-logs")
@RequiredArgsConstructor
public class CsSmsLogApiV1 {
    private final CsSmsLogBusinessService csSmsLogBusinessService;

    @GetMapping("/rental-order-infos/{rentalOrderInfoId}")
    @RequiredLogin
    public ResponseEntity<?> searchList(
            @PathVariable Map<String, Object> pathVariables
    ) {
        Message message = new Message();
        UUID rentalOrderInfoId = null;

        try{
            rentalOrderInfoId = UUID.fromString(pathVariables.get("rentalOrderInfoId").toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("주문 대상 아이디를 찾을 수 없습니다.");
        }

        message.setData(csSmsLogBusinessService.searchList(rentalOrderInfoId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
