package com.camping_rental.server.domain.rental_order_product.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.rental_order_product.projection.CountProductsProjection;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import com.camping_rental.server.domain.rental_order_product.repository.RentalOrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalOrderProductService {
    private final RentalOrderProductRepository rentalOrderProductRepository;

    public void saveAll(List<RentalOrderProductEntity> entities) {
        rentalOrderProductRepository.saveAll(entities);
    }

    public Page<RentalOrderProductProjection.JoinRentalOrderInfo> qSearchPageJoinRentalOrderInfo(Map<String, Object> params, Pageable pageable) {
        return rentalOrderProductRepository.qSelectPageJoinRentalOrderInfo(params, pageable);
    }

    public List<RentalOrderProductProjection.JoinRentalOrderInfo> qSearchListByIdsJoinRentalOrderInfo(List<UUID> ids) {
        return rentalOrderProductRepository.qSelectListByIdsJoinRentalOrderInfo(ids);
    }

    public List<RentalOrderProductEntity> searchListByRentalOrderInfoId(UUID rentalOrderInfoId) {
        return rentalOrderProductRepository.findAllByRentalOrderInfoId(rentalOrderInfoId);
    }

    public List<CountProductsProjection.Product> qCountProductsByRoomId(UUID roomId, LocalDateTime startDate, LocalDateTime endDate, List<String> orderTypes) {
        return rentalOrderProductRepository.qCountProductsByRoomId(roomId, startDate, endDate, orderTypes);
    }

    public RentalOrderProductProjection.JoinRentalOrderInfo qSearchByIdAndLenderRoomIdJoinRentalOrderInfo(UUID id, UUID roomId) {
        return rentalOrderProductRepository.qSelectByIdAndLenderRoomIdJoinRentalOrderInfo(id, roomId).orElseThrow(()->new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }
}
