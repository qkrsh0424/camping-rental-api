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
    public ResponseEntity<?> searchInfo(HttpServletRequest request) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(userBusinessService.searchUserInfo(request));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/reissue/access-token")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
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
    ) {
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
    ) {
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

        message.setStatus(HttpStatus.OK);
        message.setMessage(userBusinessService.checkDuplicateUsername(username));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/target:username_and_phoneNumber/action:check-duplicate")
    public ResponseEntity<?> checkDuplicateUsernameAndPhoneNumber(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        String username = null;
        String phoneNumber = null;
        try {
            username = body.get("username").toString();
            phoneNumber = body.get("phoneNumber").toString();
        } catch (NullPointerException e) {
            throw new NotMatchedFormatException("아이디 형식을 다시 한번 확인해 주세요.");
        }

        message.setStatus(HttpStatus.OK);
        message.setMessage(userBusinessService.checkDuplicateUsernameAndPhoneNumber(username, phoneNumber));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/action:find-username")
    public ResponseEntity<?> findUsername(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserDto.FindUsername userDto
    ){
        Message message = new Message();

        message.setData(userBusinessService.findUsername(request, response, userDto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:nickname")
    @RequiredLogin
    public ResponseEntity<?> changeNickname(
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String nickname = null;

        try{
            nickname = body.get("nickname").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("닉네임을 정확히 입력해주세요.");
        }

        userBusinessService.changeNickname(nickname);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:phoneNumber")
    @RequiredLogin
    public ResponseEntity<?> changePhoneNumber(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String phoneNumber = null;
        String phoneNumberValidationCode = null;

        try{
            phoneNumber = body.get("phoneNumber").toString();
            phoneNumberValidationCode = body.get("phoneNumberValidationCode").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("휴대전화와 인증번호를 정확히 입력해 주세요.");
        }

        userBusinessService.changePhoneNumber(request, response, phoneNumber, phoneNumberValidationCode);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:email")
    @RequiredLogin
    public ResponseEntity<?> changeEmail(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String email = null;
        String emailValidationCode = null;

        try{
            email = body.get("email").toString();
            emailValidationCode = body.get("emailValidationCode").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("휴대전화와 인증번호를 정확히 입력해 주세요.");
        }

        userBusinessService.changeEmail(request, response, email, emailValidationCode);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:password")
    @RequiredLogin
    public ResponseEntity<?> changePassword(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String password = null;
        String newPassword = null;
        String newPasswordChecker = null;

        try{
            password = body.get("password").toString();
            newPassword = body.get("newPassword").toString();
            newPasswordChecker = body.get("newPasswordChecker").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 입니다.");
        }

        userBusinessService.changePassword(request, response, password, newPassword, newPasswordChecker);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:profileImageUri")
    @RequiredLogin
    public ResponseEntity<?> changeProfileImageUri(
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        String profileImageUri = null;

        try{
            profileImageUri = body.get("profileImageUri").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("잘못된 접근 입니다.");
        }

        userBusinessService.changeProfileImageUri(profileImageUri);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
