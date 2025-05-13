package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_user.dto.query.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findComments(CommentRequestDto commentRequestDto);

    Long updateStatusById(Long id);
}
