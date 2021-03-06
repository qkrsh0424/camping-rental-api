package com.camping_rental.server.domain.user.service;

import com.camping_rental.server.config.auth.PrincipalDetails;
import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity searchByUsername(String username){
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        return userEntity;
    }

    public UserEntity searchByPhoneNumberAndLoginType(String phoneNumber, String loginType) {
        UserEntity userEntity = userRepository.findByPhoneNumberAndLoginType(phoneNumber, loginType).orElse(null);

        return userEntity;
    }

    public void saveAndModify(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public UserEntity searchByUsernameAndLoginType(String username, String loginType) {
        UserEntity userEntity = userRepository.findByUsernameAndLoginType(username,loginType).orElse(null);
        return userEntity;
    }

    public UUID getUserIdOrNull() {
        try {
            PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return principalDetails.getUser().getId();
        } catch (Exception e) {
            return null;
        }
    }

    public UUID getUserIdOrThrow() {
        try {
            PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return principalDetails.getUser().getId();
        } catch (Exception e) {
            throw new InvalidUserException("???????????? ????????? ????????? ?????????.");
        }
    }

    public UserEntity searchById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserEntity saveAndGet(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
