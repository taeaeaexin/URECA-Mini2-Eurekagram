package com.gram.eureka.eureka_gram_master.repository.custom;

public interface FeedViewRepositoryCustom {
    Long getFeedViewCount(Long feedId);
    Boolean findExistByFeedIdAndUserId(Long feedId, Long userId);
}
