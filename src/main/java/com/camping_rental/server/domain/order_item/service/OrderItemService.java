package com.camping_rental.server.domain.order_item.service;

import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import com.camping_rental.server.domain.order_item.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void saveAll(List<OrderItemEntity> entities) {
        orderItemRepository.saveAll(entities);
    }

    public List<OrderItemEntity> searchListByOrderInfoIds(List<UUID> orderInfoIds) {
        return orderItemRepository.findByOrderInfoIds(orderInfoIds);
    }
}
