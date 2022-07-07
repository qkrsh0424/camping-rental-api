package com.camping_rental.server.domain.product_category.controller;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.product_category.service.ProductCategoryBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryApiV1 {
    private final ProductCategoryBusinessService productCategoryBusinessService;

    @GetMapping("")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productCategoryBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> searchListByRoom(
            @PathVariable("roomId") Object roomIdObj
    ) {
        Message message = new Message();

        UUID roomId = null;
        try{
            roomId = UUID.fromString(roomIdObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        message.setData(productCategoryBusinessService.searchListByRoom(roomId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
