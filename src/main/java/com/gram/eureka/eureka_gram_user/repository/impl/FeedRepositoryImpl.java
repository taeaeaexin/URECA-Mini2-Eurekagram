package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedRepositoryCustom;
import com.gram.eureka.eureka_gram_user.repository.custom.UserRepositoryCustom;
import com.querydsl.core.types.Projections;
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
        return jpaQueryFactory
                .select(Projections.bean(MyFeedDto.class,
                                        feed.id,
                                        image.storedImageName))
                .from(feed)
                .join(feed, image.feed)
                .where(feed.user.id.eq(user.getId()))
                .fetch()
                .stream().toList();
    }
}
/**
 * select feed.id, image.stored_image_name
 * from feed
 * join image on feed.id = image.feed_id
 * where feed.user_id = 2; 근데 한 피드 당 이미지가 여러개.
 */