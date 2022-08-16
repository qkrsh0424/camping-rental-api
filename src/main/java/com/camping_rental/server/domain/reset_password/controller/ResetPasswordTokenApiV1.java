package com.camping_rental.server.domain.reset_password.controller;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.reset_password.dto.ResetPasswordTokenDto;
import com.camping_rental.server.domain.reset_password.service.ResetPasswordTokenBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reset-password-tokens")
@RequiredArgsConstructor
public class ResetPasswordTokenApiV1 {
    private final ResetPasswordTokenBusinessService resetPasswordTokenBusinessService;

    @GetMapping("/{id}")
    public ResponseEntity<?> checkTokenValid(
            @PathVariable("id") Object idObj
    ){
        Message message = new Message();

        UUID id = null;

        try{
            id = UUID.fromString(idObj.toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("유효하지 않은 토큰입니다.");
        }

        message.setStatus(HttpStatus.OK);
        message.setMessage(resetPasswordTokenBusinessService.checkTokenValid(id));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    public ResponseEntity<?> create(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ResetPasswordTokenDto.Create resetPasswordTokenDto
    ) {
        Message message = new Message();

        message.setData(resetPasswordTokenBusinessService.create(request, response, resetPasswordTokenDto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/{resetTokenId}/action:change")
    public ResponseEntity<?> changePassword(
            HttpServletResponse response,
            @PathVariable("resetTokenId") Object resetTokenIdObj,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        UUID resetTokenId = null;
        String password = null;
        String passwordChecker = null;

        try{
            resetTokenId = UUID.fromString(resetTokenIdObj.toString());
            password = body.get("password").toString();
            passwordChecker = body.get("passwordChecker").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 요청 입니다.");
        }

        resetPasswordTokenBusinessService.changePassword(response, resetTokenId, password, passwordChecker);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
