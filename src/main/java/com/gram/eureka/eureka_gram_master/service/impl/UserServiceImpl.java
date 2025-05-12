package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_master.dto.UserResponseDto;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // 사용자 리스트
    @Override
    public List<UserManagementDto> manageUsers() {
        List<User> users = userRepository.findAll();
        List<UserManagementDto> userManagementDtoList = users.stream()
                .map(user -> UserManagementDto.builder()
                            .userId(user.getId())
                            .userName(user.getUserName())
                            .nickName(user.getNickName())
                            .phoneNumber(user.getPhoneNumber())
                            .batch(user.getBatch())
                            .track(user.getTrack())
                            .mode(user.getMode())
                            .createdAt(user.getCreatedAt())
                            .status(user.getStatus())
                            .build()).toList();
        return userManagementDtoList;
    }

    // 사용자 상태 수정 (승인 / 차단 / 차단해제)
    @Override
    public void updateUser(Long userId, String status) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        user.setStatus(Status.valueOf(status));
        userRepository.save(user);
    }
}
