package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findActiveUserByEmail(String email);
}
