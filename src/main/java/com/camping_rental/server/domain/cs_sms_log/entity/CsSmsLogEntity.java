package com.camping_rental.server.domain.cs_sms_log.entity;


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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cs_sms_log")
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted_flag=0")
public class CsSmsLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "sms_message")
    private String smsMessage;

    @Column(name = "from_phone_number")
    private String fromPhoneNumber;

    @Column(name = "to_phone_number")
    private String toPhoneNumber;

    @Column(name = "created_by")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "rental_order_info_id")
    @Type(type = "uuid-char")
    private UUID rentalOrderInfoId;

    @Column(name = "room_id")
    @Type(type = "uuid-char")
    private UUID roomId;
}
