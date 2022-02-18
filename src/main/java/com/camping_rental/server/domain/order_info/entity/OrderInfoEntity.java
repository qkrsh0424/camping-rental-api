package com.camping_rental.server.domain.order_info.entity;

import com.camping_rental.server.domain.order_info.dto.OrderInfoDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_info")
public class OrderInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "orderer")
    private String orderer;

    @Column(name = "orderer_phone_number")
    private String ordererPhoneNumber;

    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "pickup_region")
    private String pickupRegion;

    @Column(name = "return_region")
    private String returnRegion;

    @Column(name = "pickup_time")
    private String pickupTime;

    @Column(name = "return_time")
    private String returnTime;

    @Column(name = "service_agreement_yn")
    private String serviceAgreementYn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static OrderInfoEntity toEntity(OrderInfoDto dto) {
        OrderInfoEntity entity = OrderInfoEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .orderer(dto.getOrderer())
                .ordererPhoneNumber(dto.getOrderer())
                .pickupDate(dto.getPickupDate())
                .returnDate(dto.getReturnDate())
                .pickupRegion(dto.getPickupRegion())
                .returnRegion(dto.getReturnRegion())
                .pickupTime(dto.getPickupTime())
                .returnTime(dto.getReturnTime())
                .serviceAgreementYn(dto.getServiceAgreementYn())
                .createdAt(dto.getCreatedAt())
                .build();
        return entity;
    }
}
