package com.gram.eureka.eureka_gram_master.repository.custom;

import com.gram.eureka.eureka_gram_master.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findActiveUserByEmail(String email);


}
