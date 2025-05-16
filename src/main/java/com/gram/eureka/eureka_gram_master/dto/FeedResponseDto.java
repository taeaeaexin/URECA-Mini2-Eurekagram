package com.gram.eureka.eureka_gram_master.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedResponseDto {
    private Long feedId;
    private Long feedCount;
    private List<String> images;
    private Long commentCount;
    private Long viewCount;
    private String content;

    private String nickName;
    private LocalDateTime createDate;
}
