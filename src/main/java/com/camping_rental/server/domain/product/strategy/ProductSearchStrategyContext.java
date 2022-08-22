package com.camping_rental.server.domain.product.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ProductSearchStrategyContext {
    private Map<ProductSearchStrategyName, ProductSearchStrategy> strategies;
    private ProductSearchStrategy strategy;

    @Autowired
    public ProductSearchStrategyContext(Set<ProductSearchStrategy> strategySet) {
        makeStrategies(strategySet);
    }

    public ProductSearchStrategy returnStrategy() {
        if(strategy == null){
            return strategies.get(ProductSearchStrategyName.Basic);
        }
        return strategy;
    }

    public void setStrategy(ProductSearchStrategyName strategyName) {
        strategy = strategies.get(strategyName);
    }


    private void makeStrategies(Set<ProductSearchStrategy> strategySet) {
        strategies = new HashMap<ProductSearchStrategyName, ProductSearchStrategy>();
        strategySet.forEach(strategy -> {
            strategies.put(strategy.getStrategyName(), strategy);
        });
    }
}
