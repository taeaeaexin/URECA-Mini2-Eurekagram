package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.entity.User;

import java.util.List;

public interface FeedService {
    FeedResponseDto createFeed(FeedRequestDto feedRequestDto);
//    List<MyFeedDto> myFeed();
}
