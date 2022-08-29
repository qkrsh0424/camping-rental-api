package com.camping_rental.server.domain.order_info.repository;

import com.camping_rental.server.domain.order_info.entity.OrderInfoEntity;
import com.camping_rental.server.domain.order_info.projection.OrderInfoProjection;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class OrderInfoRepositoryImplTest {
    @Autowired
    OrderInfoRepository orderInfoRepository;

    @Test
    public void qSelectList() {
        List<OrderInfoEntity> orderInfoEntities = orderInfoRepository.qSelectList();
    }

    @Test
    public void qSelectJoinItemsPage(){
       Pageable pageable = PageRequest.of(0, 10);
       Page<OrderInfoProjection.JoinOrderItems> results = orderInfoRepository.qSelectJoinOrderItemsPage(pageable);

        log.info("page total size : {}", results.getTotalElements());
        log.info("finded size : {}", results.getSize());
    }
}