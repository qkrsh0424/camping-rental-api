package com.camping_rental.server.domain.refresh_token.service;

import com.camping_rental.server.domain.refresh_token.entity.RefreshTokenEntity;
import com.camping_rental.server.domain.refresh_token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveAndModify(RefreshTokenEntity refreshTokenEntity){
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void deleteOldRefreshToken(UUID userId, Integer allowedAccessCount){
        refreshTokenRepository.deleteOldRefreshTokenForUser(userId.toString(), allowedAccessCount);
    }

    public RefreshTokenEntity searchById(UUID refreshTokenId) {
        return refreshTokenRepository.findById(refreshTokenId).orElse(null);
    }
}
