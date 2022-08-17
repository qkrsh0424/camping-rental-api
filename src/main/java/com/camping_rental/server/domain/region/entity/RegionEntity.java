package com.camping_rental.server.domain.region.entity;

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
@Table(name = "region")
@Entity
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0") // Soft Delete 적용
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "sido")
    private String sido;

    @Column(name = "sigungu")
    private String sigungu;

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "address")
    private String address;

    @Column(name = "user_selected_address")
    private String userSelectedAddress;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "bname")
    private String bname;

    @Column(name = "bname1")
    private String bname1;

    @Column(name = "bname2")
    private String bname2;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "created_by")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @Column(name = "room_cid")
    private Integer roomCid;

    @Column(name = "room_id")
    @Type(type = "uuid-char")
    private UUID roomId;
}
