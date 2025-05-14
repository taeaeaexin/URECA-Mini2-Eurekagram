package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
public class FeedRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        feedRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFeedInfoById() {

    }

    @Test
    void testFindFeedsByNickName() {
        User user = userRepository.save(User.builder()
                .email("active1@naver.com")
                .password("1234")
                .nickName("active_user1")
                .build());

        Feed feed = Feed.builder()
                .user(user)
                .content("test feed")
                .status(Status.ACTIVE)
                .build();

        feedRepository.save(feed);

        em.flush();
        em.clear();

        List<Feed> result = feedRepository.findFeedsByNickname("active_user1", null, PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).contains("feed");
    }
}