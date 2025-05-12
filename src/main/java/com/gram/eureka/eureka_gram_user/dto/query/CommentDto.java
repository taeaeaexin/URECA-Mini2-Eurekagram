package com.gram.eureka.eureka_gram_user.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CommentDto {
    private final Long commentId;
    private final String content;
    private final UserDto writer;
    private final Boolean deleteYn;

    @QueryProjection
    public CommentDto(Long commentId, String content, UserDto writer, Boolean deleteYn) {
        this.commentId = commentId;
        this.content = content;
        this.writer = writer;
        this.deleteYn = deleteYn;
    }
}
