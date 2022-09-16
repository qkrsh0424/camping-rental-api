package com.camping_rental.server.domain.rental_order_info.dto;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.rental_order_product.dto.RentalOrderProductDto;
import com.camping_rental.server.utils.CustomCookieUtils;
import com.camping_rental.server.utils.CustomJwtUtils;
import com.camping_rental.server.utils.DataFormatUtils;
import com.camping_rental.server.utils.ValidationTokenUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private String borrower;
    private String borrowerPhoneNumber;
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
    public static class Create {
        private String orderNumber;
        private String orderer;
        private String ordererPhoneNumber;
        private String borrower;
        private String borrowerPhoneNumber;
        private LocalDateTime pickupDate;
        private String pickupTime;
        private String pickupPlace;
        private LocalDateTime returnDate;
        private String returnTime;
        private String returnPlace;
        private String csMemo;
        private String serviceAgreementYn;

        private List<RentalOrderProductDto.Create> rentalOrderProducts;

        private boolean sameWithOrdererFlag;
        private String borrowerPhoneNumberValidationCode;
    }
}
