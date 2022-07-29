package com.camping_rental.server.domain.rental_order_info.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoDto;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rental-order-infos")
@RequiredArgsConstructor
public class RentalOrderInfoApiV1 {
    private final RentalOrderInfoBusinessService rentalOrderInfoBusinessService;

    @GetMapping("/orderNumber:{orderNumber}")
    public ResponseEntity<?> searchOneByOrderNumber(
            @PathVariable("orderNumber") Object orderNumberObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        String orderNumber = null;
        String orderer = null;
        String ordererPhoneNumber = null;

        try {
            orderNumber = orderNumberObj.toString();
            orderer = params.get("orderer").toString();
            ordererPhoneNumber = params.get("ordererPhoneNumber").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        message.setData(rentalOrderInfoBusinessService.searchOneByOrderNumberAndOrdererAndOrderPhoneNumber(orderNumber, orderer, ordererPhoneNumber));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/page")
    @RequiredLogin
    public ResponseEntity<?> searchPage(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable
    ){
        Message message = new Message();

        message.setData(rentalOrderInfoBusinessService.searchPage(pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
    @PostMapping("")
    public ResponseEntity<?> createOne(
            @RequestBody RentalOrderInfoDto.Create rentalOrderInfoDto
    ) {
        Message message = new Message();

        message.setData(rentalOrderInfoBusinessService.createOne(rentalOrderInfoDto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/{rentalOrderInfoId}/action:send-sms")
    @RequiredLogin
    public ResponseEntity<?> sendSms(
            @PathVariable("rentalOrderInfoId") Object rentalOrderInfoIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        String smsMessage = null;
        UUID rentalOrderInfoId = null;

        try{
            smsMessage = body.get("smsMessage").toString();
            rentalOrderInfoId = UUID.fromString(rentalOrderInfoIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        rentalOrderInfoBusinessService.sendSms(rentalOrderInfoId, smsMessage);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
