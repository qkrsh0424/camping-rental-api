package com.camping_rental.server.domain.rental_order_info.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductDto;
import com.camping_rental.server.utils.DataFormatUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalOrderInfoDto {
    private Long cid;
    private UUID id;
    private String orderNumber;
    private String orderer;
    private String ordererPhoneNumber;
    private LocalDateTime pickupDate;
    private String pickupTime;
    private String pickupPlace;
    private LocalDateTime returnDate;
    private String returnTime;
    private String returnPlace;
    private String ordererType;
    private String csMemo;
    private String serviceAgreementYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedFlag;
    private UUID ordererId;
    private UUID lenderRoomId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create{
        private String orderNumber;
        private String orderer;
        private String ordererPhoneNumber;
        private LocalDateTime pickupDate;
        private String pickupTime;
        private String pickupPlace;
        private LocalDateTime returnDate;
        private String returnTime;
        private String returnPlace;
        private String csMemo;
        private String serviceAgreementYn;

        private List<RentalOrderProductDto.Create> rentalOrderProducts;

        public static void checkFieldFormatValid(Create rentalOrderInfoDto){
            if(!rentalOrderInfoDto.getServiceAgreementYn().equals("y")){
                throw new NotMatchedFormatException("개인정보수집 및 이용에 동의 해주세요.");
            }

            if(StringUtils.isBlank(rentalOrderInfoDto.getOrderer())){
                throw new NotMatchedFormatException("주문자 이름을 정확히 입력해 주세요.");
            }

            if(StringUtils.isBlank(rentalOrderInfoDto.getOrdererPhoneNumber()) || !DataFormatUtils.isPhoneNumberFormatValid(rentalOrderInfoDto.getOrdererPhoneNumber())){
                throw new NotMatchedFormatException("주문자 연락처를 정확히 입력해 주세요.");
            }

            if(rentalOrderInfoDto.getPickupDate() == null){
                throw new NotMatchedFormatException("픽업 날짜를 선택해 주세요.");
            }

            if(StringUtils.isBlank(rentalOrderInfoDto.getPickupTime())){
                throw new NotMatchedFormatException("픽업 시간을 선택해 주세요.");
            }

            if(rentalOrderInfoDto.getReturnDate() == null){
                throw new NotMatchedFormatException("반납 날짜를 선택해 주세요.");
            }

            if(StringUtils.isBlank(rentalOrderInfoDto.getReturnTime())){
                throw new NotMatchedFormatException("반납 시간을 선택해 주세요.");
            }

            if(StringUtils.isBlank(rentalOrderInfoDto.getPickupPlace()) || StringUtils.isBlank(rentalOrderInfoDto.getReturnPlace())){
                throw new NotMatchedFormatException("픽업 | 반납 장소를 선택해 주세요.");
            }
        }
    }
}
