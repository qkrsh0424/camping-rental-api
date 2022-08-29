package com.camping_rental.server.domain.user_consent.vo;

import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.UUID;

public class UserConsentVo {
    private Integer cid;
    private UUID id;
    private String serviceTermsYn;
    private String privacyPolicyYn;
    private String marketingYn;
    private String marketingPhoneYn;
    private String marketingEmailYn;
    private boolean deletedFlag;
    private UUID userId;

    @Data
    @Builder
    public static class Basic{
        private UUID id;
        private String serviceTermsYn;
        private String privacyPolicyYn;
        private String marketingYn;
        private String marketingPhoneYn;
        private String marketingEmailYn;

        public static Basic toVo(UserConsentEntity entity){
            Basic vo = Basic.builder()
                    .id(entity.getId())
                    .serviceTermsYn(entity.getServiceTermsYn())
                    .privacyPolicyYn(entity.getPrivacyPolicyYn())
                    .marketingYn(entity.getMarketingYn())
                    .marketingPhoneYn(entity.getMarketingPhoneYn())
                    .marketingEmailYn(entity.getMarketingEmailYn())
                    .build();

            return vo;
        }
    }
}
