package com.gram.eureka.eureka_gram_user.service.impl;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_user.dto.UserResponseDto;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.repository.UserRepository;
import com.gram.eureka.eureka_gram_user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDto<UserResponseDto> join(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        UserResponseDto userResponseDto = UserResponseDto.from(
                userRepository.save(User.of(userRequestDto))
        );
        return BaseResponseDto.<UserResponseDto>builder()
                .statusCode(200)
                .message("회원가입이 성공적으로 진행되었습니다.")
                .data(userResponseDto)
                .build();
    }
}
