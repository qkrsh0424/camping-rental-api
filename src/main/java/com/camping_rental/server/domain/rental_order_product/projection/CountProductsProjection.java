package com.camping_rental.server.domain.rental_order_product.projection;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CountProductsProjection {

    @Data
    @Builder
    public static class Info {
        private List<Product> products;
        private Integer totalSum;
        private Integer length;
    }

    @Data
    public static class Product {
        private UUID productId;
        private String productName;
        private String thumbnailUri;
        private Integer unitSum;
    }
}
