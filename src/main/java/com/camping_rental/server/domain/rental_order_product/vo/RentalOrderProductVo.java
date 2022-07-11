package com.camping_rental.server.domain.rental_order_product.vo;

import com.camping_rental.server.domain.rental_order_info.vo.RentalOrderInfoVo;
import com.camping_rental.server.domain.rental_order_product.projection.RentalOrderProductProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderProductVo {
    private Long cid;
    private UUID id;
    private String status;
    private String productName;
    private String thumbnailUri;
    private Integer price;
    private Integer discountRate;
    private Integer unit;
    private boolean deletedFlag;
    private UUID rentalOrderInfoId;
    private UUID productId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinRentalOrderInfo {
        private UUID id;
        private String status;
        private String productName;
        private String thumbnailUri;
        private Integer price;
        private Integer discountRate;
        private Integer unit;
        private UUID rentalOrderInfoId;
        private UUID productId;

        private RentalOrderInfoVo.Basic rentalOrderInfo;

        public static JoinRentalOrderInfo toVo(RentalOrderProductProjection.JoinRentalOrderInfo proj) {
            JoinRentalOrderInfo vo = JoinRentalOrderInfo.builder()
                    .id(proj.getRentalOrderProductEntity().getId())
                    .status(proj.getRentalOrderProductEntity().getStatus())
                    .productName(proj.getRentalOrderProductEntity().getProductName())
                    .thumbnailUri(proj.getRentalOrderProductEntity().getThumbnailUri())
                    .price(proj.getRentalOrderProductEntity().getPrice())
                    .discountRate(proj.getRentalOrderProductEntity().getDiscountRate())
                    .unit(proj.getRentalOrderProductEntity().getUnit())
                    .rentalOrderInfoId(proj.getRentalOrderProductEntity().getRentalOrderInfoId())
                    .productId(proj.getRentalOrderProductEntity().getProductId())
                    .rentalOrderInfo(RentalOrderInfoVo.Basic.toVo(proj.getRentalOrderInfoEntity()))
                    .build();
            return vo;
        }
    }
}
