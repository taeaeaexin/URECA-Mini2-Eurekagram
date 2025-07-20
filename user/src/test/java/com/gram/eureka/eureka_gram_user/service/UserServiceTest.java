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
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @Transactional
    public void testJoinDuplicateEmail() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("active1@naver.com");

        // when
        BaseResponseDto<UserResponseDto> responseDto = userService.join(userRequestDto);

        // then
        assertEquals("이미 존재하는 이메일입니다.", responseDto.getMessage());
        assertEquals(409, responseDto.getStatusCode());
    }

    @Test
    @Order(2)
    @Transactional
    public void testJoinDuplicateNickname() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setNickName("active_user1");

        // when
        BaseResponseDto<UserResponseDto> responseDto = userService.join(userRequestDto);

        // then
        assertEquals("이미 존재하는 닉네임입니다.", responseDto.getMessage());
        assertEquals(409, responseDto.getStatusCode());
    }

    @Test
    @Order(3)
    @Transactional
    public void testJoinSuccess() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("test@naver.com");
        userRequestDto.setNickName("testuser");
        userRequestDto.setPassword("test1234");

        // when
        BaseResponseDto<UserResponseDto> responseDto = userService.join(userRequestDto);

        // then
        assertEquals("회원가입이 성공적으로 진행되었습니다.", responseDto.getMessage());
        assertEquals(200, responseDto.getStatusCode());
        assertNotNull(responseDto.getData());
        assertEquals(userRequestDto.getEmail(), responseDto.getData().getEmail());
        assertEquals(userRequestDto.getNickName(), responseDto.getData().getNickName());

    }
}
