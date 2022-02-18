package com.camping_rental.server.domain.order.vo;

import com.camping_rental.server.domain.order_info.vo.OrderInfoVo;
import com.camping_rental.server.domain.order_item.vo.OrderItemVo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVo {
    private OrderInfoVo orderInfo;
    private List<OrderItemVo> orderItems;
}
