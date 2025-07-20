package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Batch;
import com.gram.eureka.eureka_gram_master.entity.enums.Mode;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.entity.enums.Track;
import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock FeedRepository feedRepository;

    @InjectMocks UserServiceImpl userService;

    @Test
    void userListTest() {
        String status = "ACTIVE";
        String nickname = "tester";
        Pageable pageable = PageRequest.of(0, 10);

        UserManagementDto dto = UserManagementDto.builder()
                .userId(1L)
                .nickName("test")
                .status(Status.ACTIVE)
                .track(Track.BACKEND)
                .mode(Mode.OFFLINE)
                .batch(Batch.SECOND)
                .build();

        Page<UserManagementDto> mockPage = new PageImpl<>(List.of(dto), pageable, 1);

        when(userRepository.findAllUsers(status, nickname, pageable)).thenReturn(mockPage);

        Page<UserManagementDto> result = userService.userList(status, nickname, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNickName()).isEqualTo("test");
    }

    // 사용자 상태 관리
    @Test
    void updateUser_APPROVE_UserActive() {
        Long userId = 1L;
        User user = User.builder().id(userId).status(Status.PENDING).build();
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        userService.updateUser(userId, "APPROVE");

        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void updateUser_BLOCK_UserAndFeedsInactive() {
        Long userId = 2L;
        User user = User.builder().id(userId).status(Status.ACTIVE).build();

        Feed feed1 = Feed.builder().id(101L).status(Status.ACTIVE).build();
        Feed feed2 = Feed.builder().id(102L).status(Status.ACTIVE).build();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(feedRepository.findByUserId(userId)).thenReturn(List.of(feed1, feed2));

        userService.updateUser(userId, "BLOCK");

        assertThat(user.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(feed1.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(feed2.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    void updateUser_UNBLOCK_UserAndFeedsActive() {
        Long userId = 3L;
        User user = User.builder().id(userId).status(Status.INACTIVE).build();

        Feed feed1 = Feed.builder().id(201L).status(Status.INACTIVE).build();
        Feed feed2 = Feed.builder().id(202L).status(Status.INACTIVE).build();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(feedRepository.findByUserId(userId)).thenReturn(List.of(feed1, feed2));

        userService.updateUser(userId, "UNBLOCK");

        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(feed1.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(feed2.getStatus()).isEqualTo(Status.ACTIVE);
    }
}
