package com.camping_rental.server.domain.order_info.controller;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_info.service.OrderInfoBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order-infos")
@RequiredArgsConstructor
public class OrderInfoApiV1 {
    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    private final OrderInfoBusinessService orderInfoBusinessService;

    @GetMapping("/page")
    public ResponseEntity<?> searchPage(
            @RequestParam Map<String, Object> params,
            @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 20) Pageable pageable
    ) {
        Message message = new Message();

        Object passwordObj = params.get("password");

        if (passwordObj == null) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        String PASSWORD = passwordObj.toString();

        if (!PASSWORD.equals(ADMIN_PASSWORD)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        message.setData(orderInfoBusinessService.searchPage(pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/{id}/target:status")
    public ResponseEntity<?> changeStatus(
            @PathVariable("id") Object orderInfoIdObj,
            @RequestBody OrderInfoDto.OrderStatus orderStatusDto
    ) {
        Message message = new Message();

        UUID orderInfoId = null;
        try {
            orderInfoId = UUID.fromString(orderInfoIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청 입니다.");
        }

        orderInfoBusinessService.changeStatus(orderInfoId, orderStatusDto);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
