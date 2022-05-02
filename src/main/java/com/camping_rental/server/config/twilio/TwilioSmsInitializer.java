package com.camping_rental.server.config.twilio;

import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioSmsInitializer {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioSmsInitializer.class);
    private final TwilioSmsConfiguration twilioSmsConfiguration;

    @Autowired
    public TwilioSmsInitializer(TwilioSmsConfiguration twilioSmsConfiguration) {
        this.twilioSmsConfiguration = twilioSmsConfiguration;
        Twilio.init(
                twilioSmsConfiguration.getAccountSid(),
                twilioSmsConfiguration.getAuthToken()
        );

        LOGGER.info("Twilio initialized ... with account sid {} ", twilioSmsConfiguration.getAccountSid());
    }
}
