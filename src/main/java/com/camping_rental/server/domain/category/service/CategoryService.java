package com.camping_rental.server.domain.category.service;

import com.camping_rental.server.domain.category.entity.CategoryEntity;
import com.camping_rental.server.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> searchList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return categoryRepository.findAll(sort);
    }
}
