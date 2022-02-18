package com.camping_rental.server.domain.item_option.service;

import com.camping_rental.server.domain.item_option.entity.ItemOptionEntity;
import com.camping_rental.server.domain.item_option.repository.ItemOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemOptionService {
    private final ItemOptionRepository itemOptionRepository;

    @Autowired
    public ItemOptionService(
            ItemOptionRepository itemOptionRepository
    ) {
        this.itemOptionRepository = itemOptionRepository;
    }

    public void saveAll(List<ItemOptionEntity> entities) {
        itemOptionRepository.saveAll(entities);
    }

    public List<ItemOptionEntity> searchListByItemIds(List<UUID> itemIds) {
        return itemOptionRepository.findByItemIds(itemIds);
    }
}
