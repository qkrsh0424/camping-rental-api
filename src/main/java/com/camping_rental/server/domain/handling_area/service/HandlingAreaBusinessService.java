package com.camping_rental.server.domain.handling_area.service;

import com.camping_rental.server.domain.handling_area.entity.HandlingAreaEntity;
import com.camping_rental.server.domain.handling_area.vo.HandlingAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandlingAreaBusinessService {
    private final HandlingAreaService handlingAreaService;

    @Autowired
    public HandlingAreaBusinessService(
            HandlingAreaService handlingAreaService
    ) {
        this.handlingAreaService = handlingAreaService;
    }

    public Object searchAll() {
        List<HandlingAreaEntity> handlingAreaEntities = handlingAreaService.searchAll();
        List<HandlingAreaVo> handlingAreaVos = handlingAreaEntities.stream().map(r -> {
            return HandlingAreaVo.toVo(r);
        }).collect(Collectors.toList());

        return handlingAreaVos;
    }
}
