package com.camping_rental.server.domain.validation.strategy;

import javax.servlet.http.HttpServletResponse;

public interface PhoneValidaitonStrategy {
    PhoneValidationName getStrategyName();
    void sendValidationCode(HttpServletResponse response, String phoneNumber);
}
