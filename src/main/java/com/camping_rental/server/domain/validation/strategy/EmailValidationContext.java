package com.camping_rental.server.domain.validation.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EmailValidationContext {
    private Map<EmailValidationName, EmailValidationStrategy> strategies;
    private EmailValidationStrategy strategy;

    @Autowired
    EmailValidationContext(Set<EmailValidationStrategy> strategySet){
        createStrategies(strategySet);
    }

    public void setStrategy(EmailValidationName validationType) {
        strategy = strategies.get(validationType);
    }

    public void sendValidationCode(HttpServletResponse response, String email) {
        strategy.sendValidationCode(response, email);
    }

    private void createStrategies(Set<EmailValidationStrategy> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy -> {
            strategies.put(strategy.getStrategyName(), strategy);
        });
    }
}
