package com.camping_rental.server.domain.product_category.vo;

import com.camping_rental.server.domain.product_category.entity.ProductCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategoryVo {
    private Integer cid;
    private UUID id;
    private String name;
    private Integer mainCategoryCid;
    private UUID mainCategoryId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Basic {
        private UUID id;
        private String name;

        public static Basic toVo(ProductCategoryEntity entity) {
            Basic dto = Basic.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build();
            return dto;
        }
    }
}
