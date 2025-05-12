package com.gram.eureka.eureka_gram_master.repository.impl;

import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Role;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.custom.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gram.eureka.eureka_gram_master.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findActiveUserByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory.select(user)
                        .from(user)
                        .where(
                                user.email.eq(email)
                                        .and(user.status.eq(Status.ACTIVE))
                                        .and(user.role.eq(Role.ROLE_ADMIN))
                        )
                        .fetchOne()
        );
    }


}

/**
 * *** 관리자는 불러오면 안됨:
 * select *
 * from user
 * where role='ROLE_USER';
 *
 * 필터링은 백에서? 프론트에서?
 *
 */