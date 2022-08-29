package com.camping_rental.server.domain.product.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product.enums.ProductPackageYnEnum;
import com.camping_rental.server.domain.product_image.dto.ProductImageDto;
import com.camping_rental.server.domain.product_package.dto.ProductPackageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long cid;
    private UUID id;
    private String name;
    private String description;
    private String thumbnailUri;
    private Integer price;
    private Integer minimumRentalHour;
    private String discountYn;
    private Integer discountMinimumHour;
    private Integer discountRate;
    private String displayYn;
    private boolean deletedFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID productCategoryId;
    private String packageYn;
    private Integer maxOrderUnit;
    private UUID roomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String name;
        private String description;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private UUID productCategoryId;
        private String packageYn;
        private Integer maxOrderUnit;
        private List<ProductImageDto.Create> productImages;
        private List<ProductPackageDto.Create> productPackages;

        public static void checkFormValid(Create dto){
            if(dto.name == null || dto.name.length() <= 0 || dto.name.length() > 50){
                throw new NotMatchedFormatException("제품명 형식을 다시 확인해 주세요.");
            }

            if(dto.description != null && dto.description.length() > 1000){
                throw new NotMatchedFormatException("설명 형식을 다시 확인해 주세요.");
            }

            if(dto.price < 0 || dto.price > 1000000000){
                throw new NotMatchedFormatException("가격 형식을 다시 확인해 주세요.");
            }

            if(dto.minimumRentalHour <= 0 || dto.minimumRentalHour > 10000){
                throw new NotMatchedFormatException("최소 대여 가능 시간 형식을 다시 확인해 주세요.");
            }

            if(dto.maxOrderUnit <= 0 || dto.maxOrderUnit > 100){
                throw new NotMatchedFormatException("최대 대여 가능 수량 형식을 다시 확인해 주세요.");
            }

            if(dto.discountYn.equals("y") && (dto.discountMinimumHour <= 0 || dto.discountMinimumHour > 2400)){
                throw new NotMatchedFormatException("할인 적용 시간 형식을 다시 확인해 주세요.");
            }

            if(dto.discountYn.equals("y") && (dto.discountRate <= 0 || dto.discountRate > 100)){
                throw new NotMatchedFormatException("할인율 형식을 다시 확인해 주세요.");
            }

            if(dto.productCategoryId == null){
                throw new NotMatchedFormatException("카테고리 형식을 다시 확인해 주세요.");
            }

            if(dto.productImages == null || dto.productImages.size() <=0 || dto.productImages.size() > 10){
                throw new NotMatchedFormatException("이미지 등록 형식을 다시 확인해 주세요.");
            }

            if (dto.packageYn.equals(ProductPackageYnEnum.Y.getValue()) && (dto.productPackages.size() <= 0 || dto.productPackages.size() > 20)) {
                throw new NotMatchedFormatException("패키지 구성 형식을 다시 확인해 주세요.");
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update{
        private String name;
        private String description;
        private Integer price;
        private Integer minimumRentalHour;
        private String discountYn;
        private Integer discountMinimumHour;
        private Integer discountRate;
        private String displayYn;
        private UUID productCategoryId;
        private String packageYn;
        private Integer maxOrderUnit;
        private List<ProductImageDto.Update> productImages;
        private List<ProductPackageDto.Create> productPackages;
    }
}
