package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // 상태별 사용자 리스트 조회 (활성/비활성/대기/전체 사용자)
    @Override
    public List<UserManagementDto> userList(String status) {
        return userRepository.findAllUsers(status);
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
