package com.gram.eureka.eureka_gram_master.repository.custom;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findActiveUserByEmail(String email);

    Page<UserManagementDto> findAllUsers(String status, String nickName, Pageable pageable);
}
