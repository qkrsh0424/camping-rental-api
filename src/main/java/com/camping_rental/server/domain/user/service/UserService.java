package com.camping_rental.server.domain.user.service;

import com.camping_rental.server.domain.user.entity.UserEntity;
import com.camping_rental.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
