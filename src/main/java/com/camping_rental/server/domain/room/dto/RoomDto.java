package com.camping_rental.server.domain.room.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.utils.DataFormatUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {
    private Integer cid;
    private UUID id;
    private String name;
    private String phoneNumber;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID userId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String name;
        private String phoneNumber;
        private String phoneValidationCode;

        public static void checkFieldValid(Create dto){
            long nameMatcherCount = Pattern.compile("^[\\s]|[\\s]$").matcher(dto.getName()).results().count();

            /*
            룸네임 체크
             */
            if(nameMatcherCount != 0 || dto.getName().length() < 2 || dto.getName().length() > 15){
                throw new NotMatchedFormatException("룸 이름을 정확하게 입력해 주세요. 첫 글자와 마지막 글자에 공백은 허용되지 않습니다.");
            }

            /*
            휴대폰 번호 체크
             */
            if(!DataFormatUtils.isPhoneNumberFormatValid(dto.getPhoneNumber())){
                throw new NotMatchedFormatException("휴대폰 번호를 형식에 맞게 입력해 주세요.");
            }

            /*
            인증번호 체크
             */
            if(dto.getPhoneValidationCode().length() != 6){
                throw new NotMatchedFormatException("인증번호를 정확하게 입력해 주세요.");
            }
        }
    }
}
