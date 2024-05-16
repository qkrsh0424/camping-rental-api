package com.camping_rental.server.domain.naver.sens.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaverCloudSensSmsSendDto {
    private final String countryCode="82";
    private String phoneNumber;
    private String subject; // 40Bytes 까지 허용
    private String message;

    public static NaverCloudSensSmsSendDto toDto(String phoneNumber, String message){
        NaverCloudSensSmsSendDto dto = NaverCloudSensSmsSendDto.builder()
                .phoneNumber(phoneNumber)
                .subject(message.substring(0, 20))
                .message(message)
                .build();

        return dto;
    }

    public static NaverCloudSensSmsSendDto toDto(String phoneNumber, String message, String subject){
        NaverCloudSensSmsSendDto dto = NaverCloudSensSmsSendDto.builder()
                .phoneNumber(phoneNumber)
                .subject(subject)
                .message(message)
                .build();

        return dto;
    }
}
