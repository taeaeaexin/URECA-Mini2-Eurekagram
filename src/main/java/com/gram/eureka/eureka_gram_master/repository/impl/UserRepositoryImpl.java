package com.gram.eureka.eureka_gram_master.repository.impl;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.QUser;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Role;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.custom.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gram.eureka.eureka_gram_master.entity.QUser.user;

@Slf4j
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

    // 사용자 목록 조회
    @Override
    public List<UserManagementDto> findAllUsers(String status, String nickName) {

        BooleanBuilder builder = new BooleanBuilder();

        // 사용자만 조회
        builder.and(user.role.eq(Role.ROLE_USER));

        // 상태별 사용자 조회 (전체조회일 경우 이 조건문은 적용X)
        if(!status.equals("ALL"))
            builder.and(user.status.eq(Status.valueOf(status)));

        // 닉네임 검색
        if(nickName != null && !nickName.isEmpty())
            builder.and(user.nickName.like('%' + nickName + '%'));

        return jpaQueryFactory
                .select(Projections.bean(UserManagementDto.class,
                        user.id.as("userId"),
                        user.userName,
                        user.nickName,
                        user.phoneNumber,
                        user.batch,
                        user.track,
                        user.mode,
                        user.createdAt,
                        user.status))
                .from(user)
                .where(builder)
                .fetch()
                .stream().toList();
    }

}
