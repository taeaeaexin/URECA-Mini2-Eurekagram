package com.gram.eureka.eureka_gram_master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyFeedsResponseDto {
    List<MyFeedDto> feeds;
    int count; // 피드 개수
}
