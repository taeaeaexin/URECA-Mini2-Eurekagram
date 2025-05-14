package com.gram.eureka.eureka_gram_master.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Role;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

// 권한
@WithMockUser(username = "admin", roles = "ADMIN")
public class FeedStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private Long feedId;

    @BeforeEach
    void setup() {
        User user = User.builder()
                .email("feedtest@eureka.com")
                .nickName("User_FeedTest")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        userRepository.deleteAll();
        User savedUser = userRepository.save(user);

        Feed feed = Feed.builder()
                .user(savedUser)
                .content("Delete test Feed")
                .status(Status.ACTIVE)
                .build();
        feedRepository.deleteAll();
        feedId = feedRepository.save(feed).getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateFeedStatusToInactive() throws Exception {
        mockMvc.perform(delete("/feeds/{id}", feedId))
                .andExpect(status().isOk());

        Feed updatedFeed = feedRepository.findById(feedId).orElseThrow();
        assertThat(updatedFeed.getStatus()).isEqualTo(Status.INACTIVE);
    }
}
