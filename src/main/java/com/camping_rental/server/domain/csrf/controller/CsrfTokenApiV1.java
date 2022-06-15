package com.camping_rental.server.domain.csrf.controller;

import com.camping_rental.server.domain.csrf.service.CsrfTokenService;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/csrf")
@RequiredArgsConstructor
public class CsrfTokenApiV1 {
    private final CsrfTokenService csrfTokenService;

    @GetMapping("/api")
    public ResponseEntity<?> getApiCsrfToken(HttpServletRequest request, HttpServletResponse response){
        Message message = new Message();

        csrfTokenService.getCsrfToken(response);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("csrf");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/test1")
    public ResponseEntity<?> test1(HttpServletRequest request, HttpServletResponse response){
        Message message = new Message();

//        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("csrf test success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
