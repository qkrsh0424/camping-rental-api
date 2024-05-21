package com.camping_rental.server.domain.rental_order_product.dto;

import com.camping_rental.server.utils.CustomConverterUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RentalOrderProductUpdateDto {
    private UUID id;
    private String status;
    private String productName;
    private String thumbnailUri;
    private Integer price;
    private String discountYn;
    private Integer discountMinimumHour;
    private Integer discountRate;
    private Integer unit;
    private boolean deletedFlag;
    private UUID rentalOrderInfoId;
    private UUID productId;

    public void setId(String id){
        this.id = CustomConverterUtils.toUUIDElseNull(id);
    }

    public void setRentalOrderInfoId(String rentalOrderInfoId){
        this.rentalOrderInfoId = CustomConverterUtils.toUUIDElseNull(rentalOrderInfoId);
    }

    public void setProductId(String productId){
        this.productId = CustomConverterUtils.toUUIDElseNull(productId);
    }
}
