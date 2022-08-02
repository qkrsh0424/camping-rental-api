package com.camping_rental.server.domain.product.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.product.dto.ProductDto;
import com.camping_rental.server.domain.product.service.ProductBusinessService;
import com.camping_rental.server.domain.product_count_info.service.ProductCountInfoBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductApiV1 {
    private final ProductBusinessService productBusinessService;
    private final ProductCountInfoBusinessService productCountInfoBusinessService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> searchOne(
            @PathVariable("productId") Object productIdObj
    ) {
        Message message = new Message();

        UUID productId = null;

        try {
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        productCountInfoBusinessService.increseViewCount(productId);
        message.setData(productBusinessService.searchOne(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> searchListByRoom(
            @PathVariable("roomId") Object roomIdObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID roomId = null;

        try {
            roomId = UUID.fromString(roomIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        message.setData(productBusinessService.searchListByRoomId(roomId, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/page")
    public ResponseEntity<?> searchPage(
            @RequestParam Map<String, Object> params,
            @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 30) Pageable pageable
    ) {
        Message message = new Message();

        message.setData(productBusinessService.searchPage(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/ids")
    public ResponseEntity<?> searchByIds(
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        Object productIdsObj = params.get("productIds");

        if(productIdsObj.toString().isEmpty()){
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            message.setData(new ArrayList<>());
            return new ResponseEntity<>(message, message.getStatus());
        }

        List<UUID> productIds = List.of(productIdsObj.toString().split(",")).stream().map(r->{
            try{
                return UUID.fromString(r);
            } catch (IllegalArgumentException | NullPointerException e){
                throw new NotMatchedFormatException("장바구니에 등록된 상품을 찾을 수 없거나, 비정상적인 상품이 있습니다. 장바구니를 비우고 상품을 다시 등록 후 시도해 주세요.");
            }
        }).collect(Collectors.toList());

        message.setData(productBusinessService.searchListByIds(productIds));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/one/rooms/{roomId}")
    @RequiredLogin
    public ResponseEntity<?> createOne(
            @PathVariable("roomId") Object roomIdObj,
            @RequestBody ProductDto.Create productDto
    ) {
        Message message = new Message();

        UUID roomId = null;
        try {
            roomId = UUID.fromString(roomIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("룸 데이터를 찾을 수 없습니다.");
        }

        productBusinessService.createOne(roomId, productDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/{productId}")
    @RequiredLogin
    public ResponseEntity<?> updateOne(
            @PathVariable("productId") Object productIdObj,
            @RequestBody ProductDto.Update productDto
    ) {
        Message message = new Message();

        UUID productId = null;

        try {
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        productBusinessService.updateOne(productId, productDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/{productId}/target:display-yn")
    @RequiredLogin
    public ResponseEntity<?> changeDisplayYn(
            @PathVariable("productId") Object productIdObj,
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        UUID productId = null;
        String displayYn = null;

        try {
            productId = UUID.fromString(productIdObj.toString());
            displayYn = body.get("displayYn").toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        productBusinessService.changeDisplayYn(productId, displayYn);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{productId}")
    @RequiredLogin
    public ResponseEntity<?> deleteOne(
            @PathVariable("productId") Object productIdObj
    ) {
        Message message = new Message();

        UUID productId = null;

        try {
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        productBusinessService.deleteOne(productId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
