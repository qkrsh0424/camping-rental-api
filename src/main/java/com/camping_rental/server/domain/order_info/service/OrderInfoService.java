package com.camping_rental.server.domain.order_info.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.repository.OrderInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderInfoService {
    private final OrderInfoRepository orderInfoRepository;

    @Autowired
    public OrderInfoService(OrderInfoRepository orderInfoRepository) {
        this.orderInfoRepository = orderInfoRepository;
    }

    public void saveAndModify(OrderInfoEntity entity) {
        orderInfoRepository.save(entity);
    }

    public List<OrderInfoEntity> searchList() {
        List<Sort.Order> sortList = new LinkedList<>();
        sortList.add(Sort.Order.desc("createdAt"));
        Sort sort = Sort.by(sortList);
        return orderInfoRepository.findAll(sort);
    }

    public OrderInfoEntity searchOne(UUID id){
        return orderInfoRepository.findById(id).orElseThrow(() -> new NotMatchedFormatException("존재하지 않는 데이터 입니다."));
    }
}
