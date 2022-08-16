package com.camping_rental.server.domain.validation.strategy;

import com.camping_rental.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PhoneValidationContext {
    private Map<PhoneValidationName, PhoneValidaitonStrategy> strategies;
    private PhoneValidaitonStrategy strategy;

    @Autowired
    public PhoneValidationContext(Set<PhoneValidaitonStrategy> strategySet) {
        createStrategies(strategySet);
    }

    public void setStrategy(PhoneValidationName validationType) {
        strategy = strategies.get(validationType);
    }

    public void sendValidationCode(HttpServletResponse response, String phoneNumber) {
        strategy.sendValidationCode(response, phoneNumber);
    }

    private void createStrategies(Set<PhoneValidaitonStrategy> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy -> {
            strategies.put(strategy.getStrategyName(), strategy);
        });
    }
}
