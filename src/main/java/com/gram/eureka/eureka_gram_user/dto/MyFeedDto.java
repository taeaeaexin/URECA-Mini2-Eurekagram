package com.gram.eureka.eureka_gram_user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 나의 개별 피드
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFeedDto {
    private Long feedId;
    private List<String> imgName;

    private String firstImg; // service에서 first img만.
}
// feed 1 : img N
