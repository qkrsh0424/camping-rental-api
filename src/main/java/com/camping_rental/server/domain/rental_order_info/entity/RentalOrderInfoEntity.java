package com.camping_rental.server.domain.rental_order_info.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rental_order_info")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0")
public class RentalOrderInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "orderer")
    private String orderer;

    @Column(name = "orderer_phone_number")
    private String ordererPhoneNumber;

    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    @Column(name = "pickup_time")
    private String pickupTime;

    @Column(name = "pickup_place")
    private String pickupPlace;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "return_time")
    private String returnTime;

    @Column(name = "return_place")
    private String returnPlace;

    @Column(name = "orderer_type")
    private String ordererType;

    @Column(name = "cs_memo")
    private String csMemo;

    @Column(name = "service_agreement_yn")
    private String serviceAgreementYn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "orderer_id")
    @Type(type = "uuid-char")
    private UUID ordererId;

    @Column(name = "lender_room_id")
    @Type(type = "uuid-char")
    private UUID lenderRoomId;
}
