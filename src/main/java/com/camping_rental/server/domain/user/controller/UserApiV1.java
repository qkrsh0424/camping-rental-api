package com.camping_rental.server.domain.user.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.user.dto.UserDto;
import com.camping_rental.server.domain.user.service.UserBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiV1 {
    private final UserBusinessService userBusinessService;

//    @RequiredLogin
    @GetMapping("/info")
    public ResponseEntity<?> searchInfo(HttpServletRequest request){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(userBusinessService.searchUserInfo(request));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/reissue/access-token")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request, HttpServletResponse response){
        Message message = new Message();

        userBusinessService.reissueAccessToken(request, response);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserDto.LocalSignup userSignupDto
    ) {
        Message message = new Message();

        userBusinessService.signup(request, response, userSignupDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserDto.LocalLogin userLoginDto
    ){
        Message message = new Message();

        userBusinessService.login(request, response, userLoginDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        Message message = new Message();

        userBusinessService.logout(request, response);
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
