package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_user.dto.UserResponseDto;

public interface UserService {
    BaseResponseDto<UserResponseDto> join(UserRequestDto userRequestDto);
}
