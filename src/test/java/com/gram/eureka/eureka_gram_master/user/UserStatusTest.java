package com.gram.eureka.eureka_gram_master.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Role;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long testUserId;

    @BeforeEach
    void setup() {
        User user = User.builder()
                .email("testuser@eureka.com")
                .nickName("User_UserTest")
                .role(Role.ROLE_USER)
                .status(Status.PENDING)
                .build();

        userRepository.deleteAll();
        testUserId = userRepository.save(user).getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateUserStatusToActive() throws Exception {
        mockMvc.perform(put("/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "APPROVE"))))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(testUserId).orElseThrow();
        assertThat(updatedUser.getStatus()).isEqualTo(Status.ACTIVE);
    }
}
