package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_user.dto.UserResponseDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @Transactional
    public void testCreateFeed() {
        // given

        // when

        // then
    }

}
