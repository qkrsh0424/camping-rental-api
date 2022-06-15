package com.camping_rental.server.domain.order_item.repository;

import com.camping_rental.server.domain.order_item.projection.OrderItemProjection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class OrderItemRepositoryImplTest {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    public void qSelectM2OJPage(){
        Pageable pageable = PageRequest.of(1, 10);

        Page<OrderItemProjection.M2OJ> orderItemProjectionPage = orderItemRepository.qSelectM2OJPage(pageable);

        long totalCount = orderItemProjectionPage.getTotalElements();

        List<OrderItemProjection.M2OJ> orderItemProjections = orderItemProjectionPage.getContent();

        log.info("totalCount : {}", totalCount);
        log.info("size : {}", orderItemProjections.size());
        for(int i = 0; i< orderItemProjections.size(); i++){
            log.info("no({}) : {}", i, orderItemProjections.get(i).getOrderInfoEntity().getOrderer());
        }
    }
}