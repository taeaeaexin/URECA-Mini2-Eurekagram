package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.*;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);
    MyFeedsResponseDto myFeed();
}
