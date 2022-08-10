package com.camping_rental.server.domain.user_consent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_consent")
@Entity
@DynamicInsert
@DynamicUpdate
public class UserConsentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "service_terms_yn")
    private String serviceTermsYn;

    @Column(name = "privacy_policy_yn")
    private String privacyPolicyYn;

    @Column(name = "marketing_yn")
    private String marketingYn;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
}
