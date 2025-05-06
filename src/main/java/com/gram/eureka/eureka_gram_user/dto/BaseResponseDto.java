package com.gram.eureka.eureka_gram_user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponseDto<T> {
    private Integer statusCode;
    private String message;
    private T data;
}
