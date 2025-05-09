package com.gram.eureka.eureka_gram_user.controller;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/feeds")
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/")
    @ResponseBody
    public BaseResponseDto<FeedResponseDto> createFeed(@ModelAttribute FeedRequestDto feedRequestDto) {
        log.info("createFeed 의 요청값은 다음과 같습니다. {}", feedRequestDto);

        for (MultipartFile image : feedRequestDto.getImages()) {
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
            }
        }

        FeedResponseDto feed = feedService.createFeed(feedRequestDto);

        return BaseResponseDto.<FeedResponseDto>builder()
                .statusCode(200)
                .message("success")
                .data(feed)
                .build();
    }
}
