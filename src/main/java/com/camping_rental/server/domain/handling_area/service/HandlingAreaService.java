package com.camping_rental.server.domain.handling_area.service;

import com.camping_rental.server.domain.handling_area.entity.HandlingAreaEntity;
import com.camping_rental.server.domain.handling_area.repository.HandlingAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandlingAreaService {
    private final HandlingAreaRepository handlingAreaRepository;

    @Autowired
    public HandlingAreaService(
            HandlingAreaRepository handlingAreaRepository
    ) {
        this.handlingAreaRepository = handlingAreaRepository;
    }

    public List<HandlingAreaEntity> searchAll(){
        return handlingAreaRepository.findAll();
    }
}
