package com.camping_rental.server.domain.user_consent.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import com.camping_rental.server.domain.user_consent.repository.UserConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserConsentService {
    private final UserConsentRepository userConsentRepository;

    public void saveAndModify(UserConsentEntity entity){
        userConsentRepository.save(entity);
    }

    public UserConsentEntity searchByUserIdElseThrow(UUID userId) {
        return userConsentRepository.findByUserId(userId).orElseThrow(()->new NotMatchedFormatException("마케팅 정보수신 데이터를 찾을 수 없음."));
    }
}
