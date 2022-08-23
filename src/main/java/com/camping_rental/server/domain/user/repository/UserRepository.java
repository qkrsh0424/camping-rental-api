package com.camping_rental.server.domain.user.repository;

import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.enums.UserLoginTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPhoneNumberAndLoginType(String phoneNumber, String loginType);

    Optional<UserEntity> findByUsernameAndLoginType(String username, String loginType);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsernameAndPhoneNumberAndLoginType(String username, String phoneNumber, String loginType);

    Optional<UserEntity> findByEmailAndLoginType(String email, String loginType);

}
