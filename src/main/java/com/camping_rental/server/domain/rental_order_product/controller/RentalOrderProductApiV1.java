package com.camping_rental.server.domain.rental_order_product.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductUpdateDto;
import com.camping_rental.server.domain.rental_order_product.service.RentalOrderProductBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1/rental-order-products")
@RequiredArgsConstructor
public class RentalOrderProductApiV1 {
    private final RentalOrderProductBusinessService rentalOrderProductBusinessService;

    @GetMapping("/private")
    @RequiredLogin
    public ResponseEntity<?> searchPageByPrivate(
            @RequestParam Map<String, Object> params,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable
    ) {
        Message message = new Message();


        message.setData(rentalOrderProductBusinessService.searchPageByPrivate(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/rental-order-infos/{rentalOrderInfoId}")
    @RequiredLogin
    public ResponseEntity<?> searchListByRentalOrderInfoId(
            @PathVariable("rentalOrderInfoId") Object rentalOrderInfoIdObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID rentalOrderInfoId = null;

        try {
            rentalOrderInfoId = UUID.fromString(rentalOrderInfoIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("주문 정보를 찾을 수 없습니다.");
        }

        message.setData(rentalOrderProductBusinessService.searchListByRentalOrderInfoId(rentalOrderInfoId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/count/products")
    @RequiredLogin
    public ResponseEntity<?> countProducts(
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Object startDateObj = params.get("startDate");
        Object endDateObj = params.get("endDate");

        LocalDateTime startDate = LocalDateTime.parse(startDateObj.toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateObj.toString(), formatter);
        Object orderTypesObj = params.get("orderTypes");

        List<String> orderTypes = List.of(orderTypesObj.toString().split(","));

        message.setData(rentalOrderProductBusinessService.countProducts(startDate, endDate, orderTypes));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-confirmOrder")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToConfirmOrder(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToConfirmOrder(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-confirmReservation")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToConfirmReservation(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToConfirmReservation(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-pickedUp")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToPickedUp(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToPickedUp(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-returned")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToReturned(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToReturned(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-completed")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToCompleted(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToCompleted(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/target:status/action:set-cancelled")
    @RequiredLogin
    public ResponseEntity<?> changeStatusToCancelled(
            @RequestBody Map<String, Object> body
    ) {
        Message message = new Message();

        List<Object> productIdsObj = (List<Object>) body.get("productIds");
        List<UUID> productIds = new ArrayList<>();

        try {
            productIdsObj.stream().forEach(r -> {
                productIds.add(UUID.fromString(r.toString()));
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("제품 데이터를 찾을 수 없습니다.");
        }

        rentalOrderProductBusinessService.changeStatusToCancelled(productIds);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("")
    @RequiredLogin
    public ResponseEntity<?> update(
            @RequestBody RentalOrderProductUpdateDto rentalOrderProductUpdateDto
    ) {
        Message message = new Message();

        message.setData(rentalOrderProductBusinessService.update(rentalOrderProductUpdateDto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
