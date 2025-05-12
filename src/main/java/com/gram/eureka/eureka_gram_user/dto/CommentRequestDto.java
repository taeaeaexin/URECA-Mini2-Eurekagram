package com.gram.eureka.eureka_gram_user.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private Long feedId;
    private Long userId;
    private String content;
}
