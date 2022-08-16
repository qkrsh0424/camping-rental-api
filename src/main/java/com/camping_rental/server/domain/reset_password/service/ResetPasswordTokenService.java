package com.camping_rental.server.domain.reset_password.service;

import com.camping_rental.server.domain.reset_password.entity.ResetPasswordTokenEntity;
import com.camping_rental.server.domain.reset_password.repository.ResetPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenEntity saveAndModify(ResetPasswordTokenEntity entity){
        return resetPasswordTokenRepository.save(entity);
    }

    public ResetPasswordTokenEntity searchByIdOrNull(UUID id) {
        return resetPasswordTokenRepository.findById(id).orElse(null);
    }
}
