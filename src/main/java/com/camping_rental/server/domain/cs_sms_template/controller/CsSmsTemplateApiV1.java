package com.camping_rental.server.domain.cs_sms_template.controller;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.cs_sms_template.dto.CsSmsTemplateDto;
import com.camping_rental.server.domain.cs_sms_template.service.CsSmsTemplateBusinessService;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs-sms-templates")
@RequiredArgsConstructor
public class CsSmsTemplateApiV1 {
    private final CsSmsTemplateBusinessService csSmsTemplateBusinessService;

    @GetMapping("")
    @RequiredLogin
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(csSmsTemplateBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/page")
    @RequiredLogin
    public ResponseEntity<?> searchPage(
            @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 20) Pageable pageable
    ) {
        Message message = new Message();

        message.setData(csSmsTemplateBusinessService.searchPage(pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    @RequiredLogin
    public ResponseEntity<?> createOne(
            @RequestBody CsSmsTemplateDto.Create csSmsTemplateDto
    ) {
        Message message = new Message();

        csSmsTemplateBusinessService.createOne(csSmsTemplateDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/{id}/target:name")
    @RequiredLogin
    public ResponseEntity<?> changeName(
            @PathVariable("id") Object idObj,
            @RequestBody CsSmsTemplateDto.Update csSmsTemplateDto
    ) {
        Message message = new Message();

        UUID id = null;
        String name = null;

        try {
            id = UUID.fromString(idObj.toString());
            name = csSmsTemplateDto.getName();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }


        csSmsTemplateBusinessService.changeName(id, name);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/{id}/target:message")
    @RequiredLogin
    public ResponseEntity<?> changeMessage(
            @PathVariable("id") Object idObj,
            @RequestBody CsSmsTemplateDto.Update csSmsTemplateDto
    ) {
        Message message = new Message();

        UUID id = null;
        String reqMessage = null;

        try {
            id = UUID.fromString(idObj.toString());
            reqMessage = csSmsTemplateDto.getMessage();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        csSmsTemplateBusinessService.changeMessage(id, reqMessage);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{id}")
    @RequiredLogin
    public ResponseEntity<?> deleteOne(
            @PathVariable("id") Object idObj
    ) {
        Message message = new Message();

        UUID id = null;

        try {
            id = UUID.fromString(idObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        csSmsTemplateBusinessService.deleteOne(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
