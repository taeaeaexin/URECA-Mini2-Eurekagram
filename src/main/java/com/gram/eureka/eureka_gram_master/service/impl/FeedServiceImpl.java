package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.ImageRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;


}
