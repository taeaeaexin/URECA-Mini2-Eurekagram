package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);

    FeedDto detailFeed(Long id);

    FeedResponseDto updateFeed(Long id);
}
