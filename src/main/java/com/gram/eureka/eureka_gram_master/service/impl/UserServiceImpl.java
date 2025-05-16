package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;


    // 상태별 사용자 리스트 조회 (활성/비활성/대기/전체 사용자)
    @Override
    public Page<UserManagementDto> userList(String status, String nickName, Pageable pageable) {
        return userRepository.findAllUsers(status, nickName, pageable);
    }


    // 사용자 상태 및 피드 상태 수정 (승인 / 차단 / 차단해제)
    @Override
    @Transactional
    public void updateUser(Long userId, String status) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        if("APPROVE".equals(status)) { // 승인
            user.setStatus(Status.ACTIVE);

        } else if("BLOCK".equals(status)) { // 차단
            user.setStatus(Status.INACTIVE);
            feedRepository.findByUserId(userId).forEach(feed -> {
                feed.setStatus(Status.INACTIVE);
            });

        } else if("UNBLOCK".equals(status)){ // 차단 해제
            user.setStatus(Status.ACTIVE);
            feedRepository.findByUserId(userId).forEach(feed -> {
                feed.setStatus(Status.ACTIVE);
            });
        }
    }
}
