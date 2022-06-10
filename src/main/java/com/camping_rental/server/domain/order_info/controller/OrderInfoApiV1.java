package com.camping_rental.server.domain.order_info.controller;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_info.service.OrderInfoBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order-infos")
@RequiredArgsConstructor
public class OrderInfoApiV1 {
    private final OrderInfoBusinessService orderInfoBusinessService;

    @PatchMapping("/{id}/target:status")
    public ResponseEntity<?> changeStatus(
            @PathVariable("id") Object orderInfoIdObj,
            @RequestBody OrderInfoDto.OrderStatus orderStatusDto
    ) {
        Message message = new Message();

        UUID orderInfoId = null;
        try{
            orderInfoId = UUID.fromString(orderInfoIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 요청 입니다.");
        }

        orderInfoBusinessService.changeStatus(orderInfoId, orderStatusDto);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
