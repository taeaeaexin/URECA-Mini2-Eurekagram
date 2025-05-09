package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.entity.Feed;

import java.util.Optional;

public interface FeedRepositoryCustom {
    Optional<Feed> findByFeedId(Long feedId);
}
