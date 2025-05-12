package com.gram.eureka.eureka_gram_user.dto.query;

import com.gram.eureka.eureka_gram_user.entity.enums.Batch;
import com.gram.eureka.eureka_gram_user.entity.enums.Mode;
import com.gram.eureka.eureka_gram_user.entity.enums.Track;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserDto {
    private final Long userId;
    private final String nickName;
    private final Batch batch;
    private final Track track;
    private final Mode mode;

    @QueryProjection
    public UserDto(Long userId, String nickName, Batch batch, Track track, Mode mode) {
        this.userId = userId;
        this.nickName = nickName;
        this.batch = batch;
        this.track = track;
        this.mode = mode;
    }
}
