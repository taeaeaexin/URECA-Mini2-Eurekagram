package com.gram.eureka.eureka_gram_user.repository.custom;


import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {
    FeedDto findFeedInfoById(Long feedId);

    Long updateFeedStatusById(Long id);
  
    List<MyFeedDto> findFeedByUser(User user);

}
