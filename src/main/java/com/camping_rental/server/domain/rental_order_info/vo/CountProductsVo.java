package com.camping_rental.server.domain.rental_order_info.vo;

import com.camping_rental.server.domain.rental_order_product.projection.CountProductsProjection;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CountProductsVo {
    @Data
    @Builder
    public static class Basic {
        private List<Product> products;
        private Integer totalSum;
        private Integer length;

        public static Basic toVo(List<CountProductsProjection.Product> projs, Integer totalSum, Integer length) {
            Basic vo = Basic.builder()
                    .products(
                            projs.stream().map(Product::toVo).collect(Collectors.toList())
                    )
                    .totalSum(totalSum)
                    .length(length)
                    .build();

            return vo;
        }
    }

    @Data
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private String thumbnailUri;
        private Integer unitSum;

        public static Product toVo(CountProductsProjection.Product proj) {
            Product countProduct = Product.builder()
                    .productId(proj.getProductId())
                    .productName(proj.getProductName())
                    .thumbnailUri(proj.getThumbnailUri())
                    .unitSum(proj.getUnitSum())
                    .build();
            return countProduct;
        }
    }
}
