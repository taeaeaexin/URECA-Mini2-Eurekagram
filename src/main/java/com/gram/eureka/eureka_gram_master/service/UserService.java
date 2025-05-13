package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;

import java.util.List;

public interface UserService {
    List<UserManagementDto> userList(String status, String nickName);
    void updateUser(Long userId, String status);
}
