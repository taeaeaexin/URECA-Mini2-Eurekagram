package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {

    List<MyFeedDto> findFeedByUser(User user);
}
