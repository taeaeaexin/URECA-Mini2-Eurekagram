package com.gram.eureka.eureka_gram_user.init;

import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.*;
import com.gram.eureka.eureka_gram_user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // ACTIVE 사용자 3명
        for (int i = 1; i <= 3; i++) {
            User user = User.builder()
                    .userName("활성사용자" + i)
                    .email("active" + i + "@naver.com")
                    .password(passwordEncoder.encode("1234"))
                    .nickName("active_user" + i)
                    .phoneNumber("0101000" + String.format("%04d", i))
                    .batch(Batch.SECOND)
                    .track(Track.BACKEND)
                    .mode(Mode.OFFLINE)
                    .status(Status.ACTIVE)
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(user);
        }

        // PENDING 사용자 2명
        for (int i = 1; i <= 2; i++) {
            User user = User.builder()
                    .userName("대기사용자" + i)
                    .email("pending" + i + "@naver.com")
                    .password(passwordEncoder.encode("1234"))
                    .nickName("pending_user" + i)
                    .phoneNumber("0102000" + String.format("%04d", i))
                    .batch(Batch.SECOND)
                    .track(Track.BACKEND)
                    .mode(Mode.OFFLINE)
                    .status(Status.PENDING)
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(user);
        }

        // INACTIVE 사용자 2명
        for (int i = 1; i <= 2; i++) {
            User user = User.builder()
                    .userName("비활성사용자" + i)
                    .email("inactive" + i + "@naver.com")
                    .password(passwordEncoder.encode("1234"))
                    .nickName("inactive_user" + i)
                    .phoneNumber("0103000" + String.format("%04d", i))
                    .batch(Batch.SECOND)
                    .track(Track.BACKEND)
                    .mode(Mode.OFFLINE)
                    .status(Status.INACTIVE)
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(user);
        }

        // 관리자 1명
        User admin = User.builder()
                .userName("관리자")
                .email("admin@naver.com")
                .password(passwordEncoder.encode("1234"))
                .nickName("test_admin")
                .phoneNumber("01099998888")
                .status(Status.ACTIVE)
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(admin);
    }
}
