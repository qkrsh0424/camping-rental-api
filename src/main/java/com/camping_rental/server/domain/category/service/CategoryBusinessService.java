package com.camping_rental.server.domain.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryBusinessService {
    private final CategoryService categoryService;

    @Autowired
    public CategoryBusinessService(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    public Object searchList() {
        return categoryService.searchList();
    }
}
