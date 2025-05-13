package com.gram.eureka.eureka_gram_master.repository.impl;

import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.Image;
import com.gram.eureka.eureka_gram_master.repository.custom.ImageRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gram.eureka.eureka_gram_master.entity.QImage.image;


@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Image> findByFeed(Feed feed) {
        return jpaQueryFactory.select(image)
                .from(image)
                .where(
                        image.feed.eq(feed)
                )
                .fetch();
    }
}
