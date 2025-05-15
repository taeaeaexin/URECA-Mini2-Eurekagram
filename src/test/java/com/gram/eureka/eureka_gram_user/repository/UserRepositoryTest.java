package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void findActiveUserByEmail() {
        User user = User.builder()
                .email("test@naver.com")
                .password("1234")
                .nickName("test")
                .status(Status.INACTIVE)
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(user);

        Optional<User> result = userRepository.findActiveUserByEmail("test@naver.com");

        assertThat(result).isEmpty();
    }
}