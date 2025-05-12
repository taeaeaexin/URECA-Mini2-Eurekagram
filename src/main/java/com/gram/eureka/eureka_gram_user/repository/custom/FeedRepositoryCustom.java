package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;

public interface FeedRepositoryCustom {
    FeedDto findFeedInfoById(Long feedId);

    Long updateFeedStatusById(Long id);
}
