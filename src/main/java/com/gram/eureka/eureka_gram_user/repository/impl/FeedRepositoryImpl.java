package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedRepositoryCustom;
import com.gram.eureka.eureka_gram_user.repository.custom.UserRepositoryCustom;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gram.eureka.eureka_gram_user.entity.QUser.user;
import static com.gram.eureka.eureka_gram_user.entity.QFeed.feed;
import static com.gram.eureka.eureka_gram_user.entity.QImage.image;



@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MyFeedDto> findFeedByUser(User user) {
        List<MyFeedDto> result = jpaQueryFactory
                .select(Projections.bean(MyFeedDto.class,
                                        feed.id.as("feedId"),
                                        ExpressionUtils.as(
                                                JPAExpressions
                                                        .select(image.storedImageName)
                                                        .from(image)
                                                        .where(image.feed.eq(feed)
                                                                .and(image.id.eq(
                                                                        JPAExpressions
                                                                                .select(image.id.min())
                                                                                .from(image)
                                                                                .where(image.feed.eq(feed))
                                                                )))
//                                                        .limit(1) // limit 누락???
                                                , "imgName"
                                        )))
                .from(feed)
                .where(feed.user.id.eq(user.getId()))
                .fetch()
                .stream().toList();
        System.out.println("로그인한 유저 ID: " + user.getId());
        System.out.println("조회된 피드: " + result);
        return result;
    }
}
/**
 * 그냥 애초에 쿼리에서 하나만 가져오는게 나을듯.
 *
 * select feed.id, (select image.stored_image_name
 *                  from image
 *                  where feed.id = image.feed_id
 *                  limit 1)
 * from feed
 * where feed.user_id = 2;
 * => limit 안됨..
 *
 *
 * select feed.id, (select image.stored_image_name
 * 				from image
 *                 where feed.id = image.feed_id
 * 					and image.id = (select min(image.id)
 * 									from image
 *                                     where feed.id = image.feed_id)
 *                )
 * from feed
 * where feed.user_id = 2;
 *
 *
 */