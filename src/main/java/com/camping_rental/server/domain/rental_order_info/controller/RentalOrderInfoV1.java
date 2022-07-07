package com.camping_rental.server.domain.rental_order_info.controller;

import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.rental_order_info.dto.RentalOrderInfoDto;
import com.camping_rental.server.domain.rental_order_info.service.RentalOrderInfoBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rental-order-infos")
@RequiredArgsConstructor
public class RentalOrderInfoV1 {
    private final RentalOrderInfoBusinessService rentalOrderInfoBusinessService;

    @PostMapping("")
    public ResponseEntity<?> createOne(
            @RequestBody RentalOrderInfoDto.Create rentalOrderInfoDto
    ) {
        Message message = new Message();

        rentalOrderInfoBusinessService.createOne(rentalOrderInfoDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
