package com.gram.eureka.eureka_gram_master.service;


import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_master.dto.CommentResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.CommentDto;

import java.util.List;

public interface CommentService {
    BaseResponseDto<CommentResponseDto> createComment(CommentRequestDto commentRequestDto);

    BaseResponseDto<List<CommentDto>> getComment(CommentRequestDto commentRequestDto);

    BaseResponseDto<CommentResponseDto> removeComment(Long id);
}
