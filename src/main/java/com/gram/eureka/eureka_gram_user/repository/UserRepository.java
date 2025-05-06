package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
