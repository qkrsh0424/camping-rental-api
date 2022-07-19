package com.camping_rental.server.domain.rental_order_info.projection;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import com.camping_rental.server.domain.room.entity.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderInfoProjection {
    private RoomEntity roomEntity;
    private RentalOrderInfoEntity rentalOrderInfoEntity;
    private Set<RentalOrderProductEntity> rentalOrderProductEntities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FullJoin{
        private RoomEntity roomEntity;
        private RentalOrderInfoEntity rentalOrderInfoEntity;
        private Set<RentalOrderProductEntity> rentalOrderProductEntities;
    }
}
