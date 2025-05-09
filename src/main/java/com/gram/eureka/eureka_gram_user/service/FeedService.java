package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.*;
import com.gram.eureka.eureka_gram_user.entity.User;

import java.util.List;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);
    BaseResponseDto<MyFeedResponseDto> myFeed();
}
