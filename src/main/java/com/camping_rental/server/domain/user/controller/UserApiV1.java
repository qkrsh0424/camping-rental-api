package com.camping_rental.server.domain.user.controller;

import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.user.dto.UserDto;
import com.camping_rental.server.domain.user.service.UserBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiV1 {
    private final UserBusinessService userBusinessService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserDto.LocalSignup userSignupDto
    ) {
//        TODO
        Message message = new Message();

        userBusinessService.signup(request, response, userSignupDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/target:username/action:check-duplicate")
    public ResponseEntity<?> checkDuplicateUsername(@RequestBody Map<String, Object> body) {
        Message message = new Message();

        String username = null;
        try {
            username = body.get("username").toString();
        } catch (NullPointerException e) {
            throw new NotMatchedFormatException("아이디 형식을 다시 한번 확인해 주세요.");
        }

        userBusinessService.checkDuplicateUsername(username);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
