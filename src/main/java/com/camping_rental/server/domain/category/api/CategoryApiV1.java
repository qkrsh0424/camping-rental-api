package com.camping_rental.server.domain.category.api;

import com.camping_rental.server.domain.category.service.CategoryBusinessService;
import com.camping_rental.server.domain.message.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryApiV1 {
    private final CategoryBusinessService categoryBusinessService;

    @Autowired
    public CategoryApiV1(
            CategoryBusinessService categoryBusinessService
    ) {
        this.categoryBusinessService = categoryBusinessService;
    }

    @GetMapping("list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(categoryBusinessService.searchList());

        return new ResponseEntity<>(message, message.getStatus());
    }
}
