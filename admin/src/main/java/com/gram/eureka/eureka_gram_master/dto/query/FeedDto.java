package com.gram.eureka.eureka_gram_master.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class FeedDto {
    private final Long feedId;
    private final String content;
    private LocalDateTime createdDate;
    private final UserDto writer;
    private final List<ImageDto> imageDtoList;
    private Boolean deleteUpdateYn;

    @Setter
    private Long FeedViewCount;

    @QueryProjection
    public FeedDto(Long feedId, String content, LocalDateTime createdDate, UserDto writer, List<ImageDto> imageDtoList, Boolean deleteUpdateYn) {
        this.feedId = feedId;
        this.content = content;
        this.createdDate = createdDate;
        this.writer = writer;
        this.imageDtoList = imageDtoList;
        this.deleteUpdateYn = deleteUpdateYn;
    }
}
