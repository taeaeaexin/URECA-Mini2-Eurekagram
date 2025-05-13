package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_master.dto.UserResponseDto;

public interface UserService {
    BaseResponseDto<UserResponseDto> join(UserRequestDto userRequestDto);
}
