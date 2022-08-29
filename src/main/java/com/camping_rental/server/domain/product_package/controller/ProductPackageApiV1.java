package com.camping_rental.server.domain.product_package.controller;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.product_package.service.ProductPackageBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-packages")
@RequiredArgsConstructor
public class ProductPackageApiV1 {
    private final ProductPackageBusinessService productPackageBusinessService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> searchListByProductId(
            @PathVariable("productId") Object productIdObj
    ) {
        Message message = new Message();

        UUID productId = null;

        try {
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("패키지 제품을 가져올 수 없습니다.");
        }

        message.setData(productPackageBusinessService.searchListByProductId(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
