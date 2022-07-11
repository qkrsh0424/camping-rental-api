package com.camping_rental.server.domain.rental_order_product.projection;

import com.camping_rental.server.domain.rental_order_info.entity.RentalOrderInfoEntity;
import com.camping_rental.server.domain.rental_order_product.entity.RentalOrderProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderProductProjection {
    private RentalOrderProductEntity rentalOrderProductEntity;
    private RentalOrderInfoEntity rentalOrderInfoEntity;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinRentalOrderInfo{
        private RentalOrderProductEntity rentalOrderProductEntity;
        private RentalOrderInfoEntity rentalOrderInfoEntity;
    }
}
