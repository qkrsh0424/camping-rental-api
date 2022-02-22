package com.camping_rental.server.domain.item.service;

import com.camping_rental.server.domain.item.entity.ItemEntity;
import com.camping_rental.server.domain.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(
            ItemRepository itemRepository
    ) {
        this.itemRepository = itemRepository;
    }

    public void saveAndModify(ItemEntity entity) {
        itemRepository.save(entity);
    }

    public List<ItemEntity> searchList(UUID itemId) {
        return itemRepository.findById(itemId);
    }

    public List<ItemEntity> searchList(Map<String, Object> params) {
        Object categoryIdObj = params.get("categoryId");
        UUID CATEGORY_ID = null;

        if (categoryIdObj == null) {
            return itemRepository.findAll();
        }

        try {
            CATEGORY_ID = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException e) {
            return itemRepository.findAll();
        }

        List<ItemEntity> itemEntities = itemRepository.findByCategoryId(CATEGORY_ID);
        return itemEntities;
    }

    public List<ItemEntity> searchDisplayList(Map<String, Object> params) {
        Object categoryIdObj = params.get("categoryId");
        UUID CATEGORY_ID = null;

        if (categoryIdObj == null) {
            return itemRepository.findDisplayList();
        }

        try {
            CATEGORY_ID = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException e) {
            return itemRepository.findDisplayList();
        }

        List<ItemEntity> itemEntities = itemRepository.findDisplayListByCategoryId(CATEGORY_ID);
        return itemEntities;
    }

    public void deleteOne(ItemEntity itemEntity) {
        itemRepository.delete(itemEntity);
    }
}
