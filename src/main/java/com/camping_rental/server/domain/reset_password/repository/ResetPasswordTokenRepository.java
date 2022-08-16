package com.camping_rental.server.domain.reset_password.repository;

import com.camping_rental.server.domain.reset_password.entity.ResetPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordTokenEntity, Integer> {
    Optional<ResetPasswordTokenEntity> findById(UUID id);
}
