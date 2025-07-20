package com.gram.eureka.eureka_gram_user.controller;


import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedsResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/feeds")
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getFeeds(
            @RequestParam(required = false) Long lastFeedId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nickname) {
        // dto로 받기
        List<FeedResponseDto> feeds = feedService.getFeeds(lastFeedId, size, nickname);
        return ResponseEntity.ok(feeds);
    }

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


    @GetMapping("/{id}")
    @ResponseBody
    public BaseResponseDto<FeedDto> detailFeed(@PathVariable Long id) {
        log.info("detailFeed id :{}", id);
        FeedDto feedDto = feedService.detailFeed(id);
        return BaseResponseDto.<FeedDto>builder()
                .statusCode(200)
                .message("success")
                .data(feedDto)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public BaseResponseDto<FeedResponseDto> deleteFeed(@PathVariable Long id) {
        log.info("detailFeed id : {}", id);
        FeedResponseDto feedResponseDto = feedService.updateFeed(id);
        return BaseResponseDto.<FeedResponseDto>builder()
                .statusCode(200)
                .message("success")
                .data(feedResponseDto)
                .build();
    }

    @GetMapping("/my-feed")
    @ResponseBody
    public BaseResponseDto<MyFeedsResponseDto> myFeed() {
        try {
            MyFeedsResponseDto myFeedsResponseDto = feedService.myFeed();
            return BaseResponseDto.<MyFeedsResponseDto>builder()
                    .statusCode(200)
                    .message("내 피드 조회 성공")
                    .data(myFeedsResponseDto)
                    .build();

        } catch (Exception e) {
            return BaseResponseDto.<MyFeedsResponseDto>builder()
                    .statusCode(500)
                    .message("내 피드 조회 실패")
                    .data(null)
                    .build();
        }
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public BaseResponseDto<FeedResponseDto> updateFeed(@PathVariable Long id, @ModelAttribute FeedRequestDto feedRequestDto) {
        log.info("updateFeed id :{}", id);
        log.info("feedRequestDto : {}", feedRequestDto);

        List<MultipartFile> images = feedRequestDto.getImages();
        if (images != null) {
            for (MultipartFile image : images) {
                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                }
            }
        }

        feedRequestDto.setId(id);

        log.info("FeedRequestDto : {}", feedRequestDto.getRemainImageIds());

        FeedResponseDto feed = feedService.updateFeed(feedRequestDto);

        return BaseResponseDto.<FeedResponseDto>builder()
                .statusCode(200)
                .message("success")
                .data(feed)
                .build();
    }
}

