package com.camping_rental.server.domain.user.repository;

import com.camping_rental.server.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPhoneNumberAndLoginType(String phoneNumber, String loginType);
}
