package com.gram.eureka.eureka_gram_user.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeedResponseDto {
    private Long feedId;
    private Long feedCount;
    private List<String> images;
    private Long commentCount;
    private Long viewCount;
    private String content;
}
