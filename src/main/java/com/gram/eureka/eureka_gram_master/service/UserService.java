package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    Page<UserManagementDto> userList(String status, String nickName, Pageable pageable);

    void updateUser(Long userId, String status);
}
