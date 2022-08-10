package com.camping_rental.server.domain.room.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.room.dto.RoomDto;
import com.camping_rental.server.domain.room.service.RoomBusinessService;
import com.camping_rental.server.domain.validation.service.ValidationBusinessService;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomApiV1 {
    private final RoomBusinessService roomBusinessService;
    private final ValidationBusinessService validationBusinessService;

    @GetMapping("/{id}")
    public ResponseEntity<?> searchOne(@PathVariable("id") Object idObj) {
        Message message = new Message();

        UUID id = null;

        try {
            id = UUID.fromString(idObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        message.setData(roomBusinessService.searchOne(id));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PostMapping("")
    public ResponseEntity<?> createOne(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody RoomDto.Create roomDto
    ) {
        Message message = new Message();

        roomBusinessService.createOne(request, response, roomDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{roomId}/target:introduction")
    public ResponseEntity<?> changeIntroduction(
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        UUID roomId = null;
        String introduction = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
            introduction = body.get("introduction").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        roomBusinessService.changeIntroduction(roomId, introduction);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{roomId}/target:name")
    public ResponseEntity<?> changeName(
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        UUID roomId = null;
        String name = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
            name = body.get("name").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        roomBusinessService.changeName(roomId, name);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{roomId}/target:phoneNumber")
    public ResponseEntity<?> changePhoneNumber(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        UUID roomId = null;
        String phoneNumber = null;
        String phoneValidationCode = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
            phoneNumber = body.get("phoneNumber").toString();
            phoneValidationCode = body.get("phoneValidationCode").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        roomBusinessService.changePhoneNumber(request, response, roomId, phoneNumber, phoneValidationCode);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{roomId}/target:profileImageUri")
    public ResponseEntity<?> changeProfileImageUri(
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        UUID roomId = null;
        String profileImageUri = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
            profileImageUri = body.get("profileImageUri").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        roomBusinessService.changeProfileImageUri(roomId, profileImageUri);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/phone/validation-code/action:send")
    @RequiredLogin
    public ResponseEntity<?> sendPhoneValidationCode(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        String phoneNumber = null;

        try {
            phoneNumber = body.get("phoneNumber").toString();
        } catch (NullPointerException e) {
            throw new NotMatchedFormatException("휴대폰 번호를 정확히 입력해 주세요.");
        }


        /*
        불필요한 중복 요청을 막기위해 10초 마다 해당 요청을 실행 할 수 있음.
         */
        Cookie phoneValidationCookie = WebUtils.getCookie(request, CustomCookieUtils.COOKIE_NAME_PHONE_VALIDATION_TOKEN);

        String phoneValidationToken = null;
        if (phoneValidationCookie != null) {
            phoneValidationToken = phoneValidationCookie.getValue();
            DecodedJWT jwt = JWT.decode(phoneValidationToken);

            long diffTime = CustomDateUtils.getCurrentTimeMillis() - jwt.getIssuedAt().getTime();

            if (diffTime < 10000) {
                throw new NotMatchedFormatException("요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.");
            }
        }

        /*
        로직 실행
         */
        roomBusinessService.sendPhoneValidationCode(response, phoneNumber);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
