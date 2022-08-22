package com.camping_rental.server.domain.product.strategy;

public enum ProductSearchStrategyName {
    Basic("basic"),
    CategoryAndRoomAndRegionsAndImages("categoryAndRoomAndRegionsAndImages"),
    Room("room"),
    RoomAndRegions("roomAndRegions")
    ;

    private String value;

    ProductSearchStrategyName(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static ProductSearchStrategyName fromString(String text) {
        for (ProductSearchStrategyName b : ProductSearchStrategyName.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return ProductSearchStrategyName.Basic;
    }
}
