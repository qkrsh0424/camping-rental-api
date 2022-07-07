package com.camping_rental.server.domain.room.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.room.service.RoomBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomApiV1 {
    private final RoomBusinessService roomBusinessService;

    @GetMapping("/{id}")
    public ResponseEntity<?> searchOne(@PathVariable("id") Object idObj){
        Message message = new Message();

        UUID id = null;

        try{
            id = UUID.fromString(idObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        message.setData(roomBusinessService.searchOne(id));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{roomId}/target:introduction")
    public ResponseEntity<?> changeIntroduction(
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody Map<String, Object> body
    ){
        Message message = new Message();

        UUID roomId = null;
        String introduction = null;

        try{
            roomId = UUID.fromString(roomIdObj.toString());
            introduction = body.get("introduction").toString();
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        roomBusinessService.changeIntroduction(roomId, introduction);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
