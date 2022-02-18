package com.camping_rental.server.domain.order.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.order.vo.OrderVo;
import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.service.OrderInfoService;
import com.camping_rental.server.domain.order_info.vo.OrderInfoVo;
import com.camping_rental.server.domain.order_item.dto.OrderItemDto;
import com.camping_rental.server.domain.order_item.entity.OrderItemEntity;
import com.camping_rental.server.domain.order_item.service.OrderItemService;
import com.camping_rental.server.domain.order_item.vo.OrderItemVo;
import com.camping_rental.server.utils.CustomDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderBusinessService {
    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderBusinessService(
            OrderInfoService orderInfoService,
            OrderItemService orderItemService
    ) {
        this.orderInfoService = orderInfoService;
        this.orderItemService = orderItemService;
    }

    @Transactional
    public void create(OrderInfoDto orderInfoDto, List<OrderItemDto> orderItemDtos) {
        if (!orderInfoDto.getServiceAgreementYn().equals("y")) {
            throw new NotMatchedFormatException("개인정보수집 및 이용에 동의해 주세요.");
        }
        UUID ORDER_INFO_ID = UUID.randomUUID();

        orderInfoDto.setId(ORDER_INFO_ID);
        orderInfoDto.setCreatedAt(CustomDateUtils.getCurrentDateTime());

        OrderInfoEntity orderInfoEntity = OrderInfoEntity.toEntity(orderInfoDto);
        List<OrderItemEntity> orderItemEntities = orderItemDtos.stream().map(r -> {
            UUID ORDER_ITEM_ID = UUID.randomUUID();
            r.setId(ORDER_ITEM_ID);
            r.setOrderInfoId(ORDER_INFO_ID);
            return OrderItemEntity.toEntity(r);
        }).collect(Collectors.toList());

        orderInfoService.saveAndModify(orderInfoEntity);
        orderItemService.saveAll(orderItemEntities);
    }

    public Object searchList() {
        List<OrderVo> orderVos = new ArrayList<>();

        List<OrderInfoEntity> orderInfoEntities = orderInfoService.searchList();
        List<UUID> orderInfoIds = orderInfoEntities.stream().map(r -> {
            return r.getId();
        }).collect(Collectors.toList());
        List<OrderItemEntity> orderItemEntities = orderItemService.searchListByOrderInfoIds(orderInfoIds);

        orderInfoEntities.stream().forEach(orderInfo -> {
            OrderVo orderVo = new OrderVo();

            OrderInfoVo orderInfoVo = OrderInfoVo.toVo(orderInfo);
            List<OrderItemVo> orderItemVos = new ArrayList<>();

            orderItemEntities.stream().forEach(orderItem -> {
                if (orderInfo.getId().equals(orderItem.getOrderInfoId())) {
                    OrderItemVo orderItemVo = OrderItemVo.toVo(orderItem);
                    orderItemVos.add(orderItemVo);
                }
            });

            orderVo.setOrderInfo(orderInfoVo);
            orderVo.setOrderItems(orderItemVos);

            orderVos.add(orderVo);
        });

        return orderVos;
    }
}
