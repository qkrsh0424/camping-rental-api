package com.camping_rental.server.domain.item.controller;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.item.dto.ItemDto;
import com.camping_rental.server.domain.item.service.ItemBusinessService;
import com.camping_rental.server.domain.item_option.dto.ItemOptionDto;
import com.camping_rental.server.domain.message.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/item")
public class ItemApiV1 {
    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    private final ItemBusinessService itemBusinessService;

    @Autowired
    public ItemApiV1(
            ItemBusinessService itemBusinessService
    ) {
        this.itemBusinessService = itemBusinessService;
    }

    @GetMapping("/one")
    public ResponseEntity<?> searchOne(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(itemBusinessService.searchOne(params));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(itemBusinessService.searchList(params));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list/display")
    public ResponseEntity<?> searchDisplayList(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(itemBusinessService.searchDisplayList(params));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOne(@RequestBody ObjectNode objectNode) {
        Message message = new Message();

        ItemDto itemDto = null;
        String password = null;

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        try {
            itemDto = mapper.treeToValue(objectNode.get("itemDto"), ItemDto.class);
//            itemOptionDtos = mapper.treeToValue(objectNode.get("itemOptionDtos"), mapper.getTypeFactory().constructCollectionType(List.class, ItemOptionDto.class));
            password = objectNode.get("password").asText();
        } catch (JsonProcessingException e) {
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        }

        if (!ADMIN_PASSWORD.equals(password)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        itemBusinessService.createOne(itemDto);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOne(@RequestBody ObjectNode objectNode) {
        Message message = new Message();

        ItemDto itemDto = null;
        String password = null;

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        mapper.registerModule(new JavaTimeModule());
        try {
            itemDto = mapper.treeToValue(objectNode.get("itemDto"), ItemDto.class);
            password = objectNode.get("password").asText();
        } catch (JsonProcessingException e) {
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        }

        if (!ADMIN_PASSWORD.equals(password)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        itemBusinessService.updateOne(itemDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOne(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        Object itemIdObj = params.get("itemId");
        Object passwordObj = params.get("password");

        UUID itemId = null;
        String password = null;

        try {
            itemId = UUID.fromString(itemIdObj.toString());
            password = passwordObj.toString();
        } catch (IllegalArgumentException e) {
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        } catch (NullPointerException e) {
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        }

        if (!ADMIN_PASSWORD.equals(password)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        itemBusinessService.deleteOne(itemId);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
