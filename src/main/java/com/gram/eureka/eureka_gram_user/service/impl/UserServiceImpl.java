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

        // 이메일, 닉네임 중복 검사
        boolean isDuplicatedEmail = userRepository.existsByEmail(userRequestDto.getEmail());
        boolean isDuplicatedNickName = userRepository.existsByNickName(userRequestDto.getNickName());

        if(isDuplicatedEmail) {
            return BaseResponseDto.<UserResponseDto>builder()
                    .statusCode(409)
                    .message("이미 존재하는 이메일입니다.")
                    .data(null)
                    .build();
        }
        if(isDuplicatedNickName) {
            return BaseResponseDto.<UserResponseDto>builder()
                    .statusCode(409)
                    .message("이미 존재하는 닉네임입니다.")
                    .data(null)
                    .build();
        }

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
