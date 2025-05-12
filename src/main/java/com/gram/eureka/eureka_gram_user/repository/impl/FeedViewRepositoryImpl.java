package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.repository.custom.FeedViewRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gram.eureka.eureka_gram_user.entity.QFeedView.feedView;

@Repository
@RequiredArgsConstructor
public class FeedViewRepositoryImpl implements FeedViewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getFeedViewCount(Long feedId) {
        return jpaQueryFactory.select(feedView.count())
                .from(feedView)
                .where(feedView.id.eq(feedId))
                .fetchOne();
    }
}
