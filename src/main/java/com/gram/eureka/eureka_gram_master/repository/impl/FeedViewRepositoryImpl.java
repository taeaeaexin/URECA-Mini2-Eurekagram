package com.gram.eureka.eureka_gram_master.repository.impl;

import com.gram.eureka.eureka_gram_master.entity.FeedView;
import com.gram.eureka.eureka_gram_master.repository.custom.FeedViewRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gram.eureka.eureka_gram_master.entity.QFeedView.feedView;


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

    @Override
    public Boolean findExistByFeedIdAndUserId(Long feedId, Long userId) {
        List<FeedView> feedViewList = jpaQueryFactory
                .select(feedView)
                .from(feedView)
                .where(
                        feedView.feed.id.eq(feedId).and(
                                feedView.user.id.eq(userId)
                        )
                )
                .fetch();

        return feedViewList.isEmpty();
    }
}
