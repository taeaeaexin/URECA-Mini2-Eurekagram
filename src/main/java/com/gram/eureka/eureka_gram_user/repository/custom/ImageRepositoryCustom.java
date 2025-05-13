package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.Image;

import java.util.List;

public interface ImageRepositoryCustom {

    List<Image> findByFeed(Feed feed);

    void updateStatusByIds(List<Long> ids);
}
