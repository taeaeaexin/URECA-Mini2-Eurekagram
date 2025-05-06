package com.gram.eureka.eureka_gram_user.controller;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_user.dto.UserResponseDto;
import com.gram.eureka.eureka_gram_user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    @ResponseBody
    public BaseResponseDto<UserResponseDto> join(@RequestBody UserRequestDto userRequestDto) {
        log.info("회원 가입 입니다. 현재 요청된 데이터는 다음과 같습니다 : {}", userRequestDto);
        // TODO 닉네임 중복 체크 필요
        // TODO 이메일 중복 체크 필요
        // 위 조건에서 문제있는 경우 statusCode, message 값 따로 설정해서 반환

        /**
         * {
         *   "userName": "서보인",
         *   "email": "sbi1024@naver.com",
         *   "password": "1234",
         *   "nickName": "길동이",
         *   "batch": "FIRST",
         *   "track": "BACKEND",
         *   "mode": "ONLINE"
         * }
         */

        return userService.join(userRequestDto);
    }
}
