package com.camping_rental.server.domain.user_consent.service;

import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import com.camping_rental.server.domain.user_consent.repository.UserConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConsentService {
    private final UserConsentRepository userConsentRepository;

    public void saveAndModify(UserConsentEntity entity){
        userConsentRepository.save(entity);
    }

}
