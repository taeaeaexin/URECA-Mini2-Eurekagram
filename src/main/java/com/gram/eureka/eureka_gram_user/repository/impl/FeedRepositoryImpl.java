package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gram.eureka.eureka_gram_user.entity.QFeed.feed;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Feed> findByFeedId(Long feedId) {
        return Optional.ofNullable(
                jpaQueryFactory.select(feed)
                        .from(feed)
                        .where(
                                feed.id.eq(feedId)
                                        .and(feed.status.eq(Status.ACTIVE))
                        ).fetchOne());
    }
}
