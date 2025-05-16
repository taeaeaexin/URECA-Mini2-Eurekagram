package com.gram.eureka.eureka_gram_master.repository.custom;


import com.gram.eureka.eureka_gram_master.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_master.dto.query.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findComments(CommentRequestDto commentRequestDto);

    Long updateStatusById(Long id);
}
