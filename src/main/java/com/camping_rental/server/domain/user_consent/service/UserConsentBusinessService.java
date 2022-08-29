package com.camping_rental.server.domain.user_consent.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.service.UserService;
import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import com.camping_rental.server.domain.user_consent.vo.UserConsentVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserConsentBusinessService {
    private final UserService userService;
    private final UserConsentService userConsentService;

    @Transactional(readOnly = true)
    public Object searchMy() {
        UUID userId = userService.getUserIdOrThrow();

        UserConsentEntity userConsentEntity = userConsentService.searchByUserIdElseThrow(userId);
        UserConsentVo.Basic userConsentVo = UserConsentVo.Basic.toVo(userConsentEntity);

        return userConsentVo;
    }

    @Transactional
    public void changeMarketingPhoneYn(String marketingPhoneYn) {
        UUID userId = userService.getUserIdOrThrow();

        UserConsentEntity userConsentEntity = userConsentService.searchByUserIdElseThrow(userId);

        userConsentEntity.setMarketingPhoneYn(marketingPhoneYn);
    }

    @Transactional
    public void changeMarketingEmailYn(String marketingEmailYn) {
        UUID userId = userService.getUserIdOrThrow();

        if (marketingEmailYn.equals("y")) {
            UserEntity userEntity = userService.searchById(userId);
            if (StringUtils.isBlank(userEntity.getEmail())) {
                throw new NotMatchedFormatException("이메일을 먼저 등록해 주세요.");
            }
        }

        UserConsentEntity userConsentEntity = userConsentService.searchByUserIdElseThrow(userId);

        userConsentEntity.setMarketingEmailYn(marketingEmailYn);
    }
}
