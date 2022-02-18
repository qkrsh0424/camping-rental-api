package com.camping_rental.server.domain.order.controller;

import com.amazonaws.Response;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.item.dto.ItemDto;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.order.service.OrderBusinessService;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_item.dto.OrderItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
public class OrderApiV1 {
    private final OrderBusinessService orderBusinessService;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    @Autowired
    public OrderApiV1(
            OrderBusinessService orderBusinessService
    ) {
        this.orderBusinessService = orderBusinessService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ObjectNode objectNode) {
        Message message = new Message();

        OrderInfoDto orderInfoDto = null;
        List<OrderItemDto> orderItemDtos = null;

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        mapper.registerModule(new JavaTimeModule());
        try {
            orderInfoDto = mapper.treeToValue(objectNode.get("orderInfoDto"), OrderInfoDto.class);
            orderItemDtos = mapper.treeToValue(objectNode.get("orderItemDtos"), mapper.getTypeFactory().constructCollectionType(List.class, OrderItemDto.class));
        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        }

        System.out.println(orderInfoDto);
        System.out.println(orderItemDtos);

        orderBusinessService.create(orderInfoDto, orderItemDtos);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        Object passwordObj = params.get("password");

        if (passwordObj == null) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        String PASSWORD = passwordObj.toString();

        if (!PASSWORD.equals(ADMIN_PASSWORD)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(orderBusinessService.searchList());

        return new ResponseEntity<>(message, message.getStatus());
    }
}
