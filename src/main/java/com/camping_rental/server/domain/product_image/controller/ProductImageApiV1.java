package com.camping_rental.server.domain.product_image.controller;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.product_image.service.ProductImageBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
public class ProductImageApiV1 {
    private final ProductImageBusinessService productImageBusinessService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> searchListByProductId(
            @PathVariable("productId") Object productIdObj
    ){
        Message message = new Message();

        UUID productId = null;
        try{
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("제품 이미지 데이터를 찾을 수 없습니다.");
        }
        message.setData(productImageBusinessService.searchListByProductId(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
