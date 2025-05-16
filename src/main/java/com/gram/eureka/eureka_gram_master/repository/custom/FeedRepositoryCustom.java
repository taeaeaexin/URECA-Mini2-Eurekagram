package com.gram.eureka.eureka_gram_master.repository.custom;


import com.gram.eureka.eureka_gram_master.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_master.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedRepositoryCustom {
    FeedDto findFeedInfoById(Long feedId, Long userId);

    Long updateFeedStatusById(Long id);

    List<MyFeedDto> findFeedByUser(User user);


    List<Feed> findFeeds(Long lastFeedId, Pageable pageable);

    List<Feed> findFeedsByNickname(String nickname, Long lastFeedId, Pageable pageable);
}
