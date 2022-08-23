package com.camping_rental.server.domain.validation.strategy;

import javax.servlet.http.HttpServletResponse;

public interface EmailValidationStrategy {
    EmailValidationName getStrategyName();
    void sendValidationCode(HttpServletResponse response, String email);
}
