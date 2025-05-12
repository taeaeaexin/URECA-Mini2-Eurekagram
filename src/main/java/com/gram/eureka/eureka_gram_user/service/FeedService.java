package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.*;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;

import java.util.List;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);

    FeedDto detailFeed(Long id);

    FeedResponseDto updateFeed(Long id);
  
    MyFeedsResponseDto myFeed();

    List<FeedResponseDto> getFeeds(Long lastFeedId, int size, String nickname);
    
}
