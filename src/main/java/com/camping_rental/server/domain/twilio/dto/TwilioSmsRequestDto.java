package com.camping_rental.server.domain.twilio.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwilioSmsRequestDto {
    private final String countryCode = "+82";

    private String phoneNumber;
    private String message;

    public String getFullPhoneNumber(){
        return this.countryCode+this.phoneNumber;
    }

    public static TwilioSmsRequestDto toDto(String phoneNumber, String message){
        TwilioSmsRequestDto dto = TwilioSmsRequestDto.builder()
                .phoneNumber(phoneNumber)
                .message(message)
                .build();

        return dto;
    }
}
