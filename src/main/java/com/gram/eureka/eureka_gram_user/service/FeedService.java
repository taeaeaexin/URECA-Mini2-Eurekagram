package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);
}
