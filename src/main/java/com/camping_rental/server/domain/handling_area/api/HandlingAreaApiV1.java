package com.camping_rental.server.domain.handling_area.api;

import com.camping_rental.server.domain.handling_area.service.HandlingAreaBusinessService;
import com.camping_rental.server.domain.message.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/handling-area")
public class HandlingAreaApiV1 {
    private final HandlingAreaBusinessService handlingAreaBusinessService;

    @Autowired
    public HandlingAreaApiV1(
            HandlingAreaBusinessService handlingAreaBusinessService
    ) {
        this.handlingAreaBusinessService = handlingAreaBusinessService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(handlingAreaBusinessService.searchAll());

        return new ResponseEntity<>(message, message.getStatus());
    }
}
