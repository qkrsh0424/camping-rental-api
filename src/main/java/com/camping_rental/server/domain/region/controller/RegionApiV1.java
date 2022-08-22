package com.camping_rental.server.domain.region.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.region.dto.RegionDto;
import com.camping_rental.server.domain.region.service.RegionBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionApiV1 {
    private final RegionBusinessService regionBusinessService;

    @GetMapping("/rooms/{roomId}")
    @RequiredLogin
    public ResponseEntity<?> searchListBySelf(@PathVariable("roomId") Object roomIdObj){
        Message message = new Message();

        UUID roomId = null;
        try{
            roomId = UUID.fromString(roomIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("데이터를 찾을 수 없습니다.");
        }

        message.setData(regionBusinessService.searchListByRoomId(roomId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/sidos/sigungus")
    public ResponseEntity<?> searchListSidoAndSigungus(){
        Message message = new Message();

        message.setData(regionBusinessService.searchListSidoAndSigungus());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    @RequiredLogin
    public ResponseEntity<?> create(@RequestBody RegionDto.Create regionDto){
        Message message = new Message();

        regionBusinessService.create(regionDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/{id}")
    @RequiredLogin
    public ResponseEntity<?> update(
            @PathVariable("id") Object idObj,
            @RequestBody RegionDto regionDto
    ){
        Message message = new Message();

        UUID id = null;

        try{
            id = UUID.fromString(idObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        regionBusinessService.update(id, regionDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{id}")
    @RequiredLogin
    public ResponseEntity<?> delete(@PathVariable("id") Object idObj){
        Message message = new Message();

        UUID id = null;

        try{
            id = UUID.fromString(idObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        regionBusinessService.delete(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
