package com.camping_rental.server.domain.ping.controller;

import com.camping_rental.server.domain.message.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class PingApiV1 {
    @GetMapping("")
    public ResponseEntity<?> ping() {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("pong");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
