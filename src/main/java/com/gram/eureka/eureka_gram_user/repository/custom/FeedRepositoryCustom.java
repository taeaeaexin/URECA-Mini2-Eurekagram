package com.gram.eureka.eureka_gram_user.repository.custom;


import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedRepositoryCustom {
    FeedDto findFeedInfoById(Long feedId, Long userId);

    Long updateFeedStatusById(Long id);

    List<MyFeedDto> findFeedByUser(User user);


    List<Feed> findFeeds(Long lastFeedId, Pageable pageable);

    List<Feed> findFeedsByNickname(String nickname, Long lastFeedId, Pageable pageable);
}
