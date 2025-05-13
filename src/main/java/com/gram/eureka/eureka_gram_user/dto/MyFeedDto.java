package com.gram.eureka.eureka_gram_user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 나의 개별 피드
@Data
public class MyFeedDto {
    private Long feedId;
    private String imgName;
}
