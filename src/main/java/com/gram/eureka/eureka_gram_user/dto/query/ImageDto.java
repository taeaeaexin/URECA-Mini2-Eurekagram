package com.gram.eureka.eureka_gram_user.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ImageDto {
    private final Long imageId;
    private final String originalImageName;
    private final String storedImageName;
    private final String imageExtension;

    @QueryProjection
    public ImageDto(Long imageId, String originalImageName, String storedImageName, String imageExtension) {
        this.imageId = imageId;
        this.originalImageName = originalImageName;
        this.storedImageName = storedImageName;
        this.imageExtension = imageExtension;
    }
}
