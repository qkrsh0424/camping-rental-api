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
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.*;
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
        itemDto.setDisplayYn("y");
        itemDto.setCreatedAt(CustomDateUtils.getCurrentDateTime());

        ItemEntity itemEntity = ItemEntity.toEntity(itemDto);

        itemService.saveAndModify(itemEntity);
    }

    public void updateOne(ItemDto itemDto) {
        ItemEntity itemEntity = itemService.searchList(itemDto.getId()).stream().findFirst().orElseThrow(() -> new NotMatchedFormatException("요청하신 데이터를 찾을 수 없습니다."));

        itemEntity.setName(itemDto.getName());
        itemEntity.setDescription(itemDto.getDescription());
        itemEntity.setThumbnailOriginName(itemDto.getThumbnailOriginName());
        itemEntity.setThumbnailName(itemDto.getThumbnailName());
        itemEntity.setThumbnailFullUri(itemDto.getThumbnailFullUri());
        itemEntity.setThumbnailPath(itemDto.getThumbnailPath());
        itemEntity.setRentalRegions(itemDto.getRentalRegions());
        itemEntity.setReturnRegions(itemDto.getReturnRegions());
        itemEntity.setPrice(itemDto.getPrice());
        itemEntity.setDiscountRate(itemDto.getDiscountRate());
        itemEntity.setDisplayYn(itemDto.getDisplayYn());
        itemEntity.setCategoryId(itemDto.getCategoryId());
        itemEntity.setCategoryName(itemDto.getCategoryName());

        itemService.saveAndModify(itemEntity);
    }

    public Object searchOne(Map<String, Object> params) {
        Object idObj = params.get("id");

        UUID id = null;
        try {
            id = UUID.fromString(idObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("요청하신 데이터를 찾을 수 없습니다.");
        }

        ItemEntity itemEntity = itemService.searchList(id).stream().findFirst().orElseThrow(() -> new NotMatchedFormatException("요청하신 데이터를 찾을 수 없습니다."));
        ItemVo itemVo = ItemVo.toVo(itemEntity);

        return itemVo;
    }

    public Object searchList(Map<String, Object> params) {
        List<ItemEntity> itemEntities = itemService.searchList(params);

        List<ItemVo> itemVos = itemEntities.stream().map(itemEntity -> {
            ItemVo itemVo = ItemVo.toVo(itemEntity);
            return itemVo;
        }).collect(Collectors.toList());

        return itemVos;
    }

    public Object searchDisplayList(Map<String, Object> params) {
        List<ItemEntity> itemEntities = itemService.searchDisplayList(params);

        List<ItemVo> itemVos = itemEntities.stream().map(itemEntity -> {
            ItemVo itemVo = ItemVo.toVo(itemEntity);
            return itemVo;
        }).collect(Collectors.toList());

        return itemVos;
    }

    public void deleteOne(UUID itemId) {
        ItemEntity itemEntity = itemService.searchList(itemId).stream().findFirst().orElseThrow(() -> new NotMatchedFormatException("요청하신 데이터를 찾을 수 없습니다."));
        itemService.deleteOne(itemEntity);
    }

}
