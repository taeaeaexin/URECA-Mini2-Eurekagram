package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_user.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_user.dto.CommentResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.CommentDto;

import java.util.List;

public interface CommentService {
    BaseResponseDto<CommentResponseDto> createComment(CommentRequestDto commentRequestDto);

    BaseResponseDto<List<CommentDto>> getComment(CommentRequestDto commentRequestDto);

    BaseResponseDto<CommentResponseDto> removeComment(Long id);
}
