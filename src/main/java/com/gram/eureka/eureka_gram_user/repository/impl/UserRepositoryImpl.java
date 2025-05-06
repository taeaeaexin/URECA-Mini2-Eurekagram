package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.repository.custom.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gram.eureka.eureka_gram_user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory.select(user)
                        .from(user)
                        .where(user.email.eq(email))
                        .fetchOne()
        );
    }
}
