package com.gram.eureka.eureka_gram_master.repository.custom;


import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.Image;

import java.util.List;

public interface ImageRepositoryCustom {

    List<Image> findByFeed(Feed feed);
}
