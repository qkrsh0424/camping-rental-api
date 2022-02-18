package com.camping_rental.server.domain.item.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.item.dto.ItemDto;
import com.camping_rental.server.domain.item.entity.ItemEntity;
import com.camping_rental.server.domain.item.vo.ItemVo;
import com.camping_rental.server.domain.item_option.dto.ItemOptionDto;
import com.camping_rental.server.domain.item_option.entity.ItemOptionEntity;
import com.camping_rental.server.domain.item_option.service.ItemOptionService;
import com.camping_rental.server.domain.item_option.vo.ItemOptionVo;
import com.camping_rental.server.utils.CustomDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemBusinessService {
    private final ItemService itemService;
    private final ItemOptionService itemOptionService;

    @Autowired
    public ItemBusinessService(
            ItemService itemService,
            ItemOptionService itemOptionService
    ) {
        this.itemService = itemService;
        this.itemOptionService = itemOptionService;
    }

    public void createOne(ItemDto itemDto) {
        UUID ITEM_ID = UUID.randomUUID();
        itemDto.setId(ITEM_ID);
        itemDto.setCreatedAt(CustomDateUtils.getCurrentDateTime());

        ItemEntity itemEntity = ItemEntity.toEntity(itemDto);

        itemService.saveAndModify(itemEntity);
    }

    public Object searchList(Map<String, Object> params) {
        List<ItemEntity> itemEntities = itemService.searchList(params);

        List<ItemVo> itemVos = itemEntities.stream().map(itemEntity ->{
            ItemVo itemVo = ItemVo.toVo(itemEntity);
            return itemVo;
        }).collect(Collectors.toList());

        return itemVos;

    }
}
