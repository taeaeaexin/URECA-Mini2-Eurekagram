package com.gram.eureka.eureka_gram_master.controller;

import com.gram.eureka.eureka_gram_master.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/feeds")
public class FeedController {
    private final FeedService feedService;

}
