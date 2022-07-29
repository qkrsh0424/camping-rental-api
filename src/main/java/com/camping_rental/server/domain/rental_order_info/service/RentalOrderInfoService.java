package com.camping_rental.server.domain.rental_order_info.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_info.projection.RentalOrderInfoProjection;
import com.camping_rental.server.domain.rental_order_info.repository.RentalOrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalOrderInfoService {
    private final RentalOrderInfoRepository rentalOrderInfoRepository;

    public void saveAndModify(RentalOrderInfoEntity entity){
        rentalOrderInfoRepository.save(entity);
    }

    public RentalOrderInfoProjection.FullJoin qSearchOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(String orderNumber, String orderer, String ordererPhoneNumber) {
        return rentalOrderInfoRepository.qSelectOneFullJoinByOrderNumberAndOrdererAndOrdererPhoneNumber(orderNumber, orderer, ordererPhoneNumber).orElseThrow(()-> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public Page<RentalOrderInfoProjection.FullJoin> qSearchPageFullJoinByRoomId(UUID roomId, Pageable pageable) {
        return rentalOrderInfoRepository.qSelectPageFullJoinByRoomId(roomId, pageable);
    }

    public RentalOrderInfoEntity searchByIdOrThrow(UUID rentalOrderInfoId) {
        return rentalOrderInfoRepository.findById(rentalOrderInfoId).orElseThrow(() -> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }
}
