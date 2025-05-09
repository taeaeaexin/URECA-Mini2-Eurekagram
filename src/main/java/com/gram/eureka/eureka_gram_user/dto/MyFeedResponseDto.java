package com.gram.eureka.eureka_gram_user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyFeedResponseDto {
    List<MyFeedDto> feeds;
    int count; // 피드 개수
}
