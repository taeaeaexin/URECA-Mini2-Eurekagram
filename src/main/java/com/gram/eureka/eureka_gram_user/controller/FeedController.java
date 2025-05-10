package com.gram.eureka.eureka_gram_user.controller;

import com.gram.eureka.eureka_gram_user.dto.*;
import com.gram.eureka.eureka_gram_user.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/my-feed")
    @ResponseBody
    public BaseResponseDto<MyFeedsResponseDto> myFeed() {

        try{
            MyFeedsResponseDto myFeedsResponseDto = feedService.myFeed();

            return BaseResponseDto.<MyFeedsResponseDto>builder()
                    .statusCode(200)
                    .message("내 피드 조회 성공")
                    .data(myFeedsResponseDto)
                    .build();

        } catch (Exception e){

            return BaseResponseDto.<MyFeedsResponseDto>builder()
                    .statusCode(500)
                    .message("내 피드 조회 실패")
                    .data(null)
                    .build();
        }
    }
}
