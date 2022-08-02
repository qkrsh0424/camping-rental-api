package com.camping_rental.server.domain.product.strategy;

public enum ProductSearchPageStrategyName {
    OrderByRank("order_rank"),
    OrderByNew("order_new")
    ;

    private String value;

    ProductSearchPageStrategyName(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static ProductSearchPageStrategyName fromString(String text) {
        for (ProductSearchPageStrategyName b : ProductSearchPageStrategyName.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return ProductSearchPageStrategyName.OrderByRank;
    }
}
