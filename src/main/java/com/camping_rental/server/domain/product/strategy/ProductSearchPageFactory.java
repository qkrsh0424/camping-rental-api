package com.camping_rental.server.domain.product.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ProductSearchPageFactory {
    private Map<ProductSearchPageStrategyName, ProductSearchPageStrategy> strategies;

    @Autowired
    public ProductSearchPageFactory(Set<ProductSearchPageStrategy> strategySet){
        createStrategy(strategySet);
    }

    public ProductSearchPageStrategy findStrategy(ProductSearchPageStrategyName strategyName) {
        return strategies.get(strategyName);
    }

    private void createStrategy(Set<ProductSearchPageStrategy> strategySet){
        strategies = new HashMap<ProductSearchPageStrategyName, ProductSearchPageStrategy>();
        strategySet.forEach(strategy ->{
            strategies.put(strategy.getStrategyName(), strategy);
        });
    }
}
