package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.dto.query.ImageDto;
import com.gram.eureka.eureka_gram_user.dto.query.UserDto;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gram.eureka.eureka_gram_user.entity.QFeed.feed;
import static com.gram.eureka.eureka_gram_user.entity.QImage.image;
import static com.gram.eureka.eureka_gram_user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FeedDto findFeedInfoById(Long feedId, Long userId) {
        List<Tuple> fetch = jpaQueryFactory.select(
                        feed.id, feed.content,
                        user.id, user.userName, user.email, user.nickName, user.batch, user.track, user.mode,
                        image.id, image.originalImageName, image.storedImageName, image.imageExtension
                )
                .from(feed)
                .innerJoin(feed.user, user)
                .leftJoin(image).on(image.feed.eq(feed))
                .where(
                        feed.id.eq(feedId)
                                .and(feed.status.eq(Status.ACTIVE))
                )
                .fetch();

        // ── 3. Stream 그룹핑으로 DTO 완성 ───────────────
        Map<Long, FeedDto> map = new LinkedHashMap<>();

        for (Tuple t : fetch) {
            Long tupleFeedId = t.get(feed.id);
            Boolean deleteUpdateYn;
            if (t.get(user.id).equals(userId)) {
                deleteUpdateYn = true;
            } else {
                deleteUpdateYn = false;
            }
            /* feedId 당 DTO 1개만 */
            FeedDto dto = map.computeIfAbsent(
                    tupleFeedId,
                    dtoId -> new FeedDto(
                            dtoId,
                            t.get(feed.content),
                            new UserDto(
                                    t.get(user.id),
                                    t.get(user.nickName),
                                    t.get(user.batch),
                                    t.get(user.track),
                                    t.get(user.mode)
                            ),
                            new ArrayList<>(),
                            deleteUpdateYn
                    )
            );

            /* 이미지 있는 행이면 리스트에 추가 */
            Long imgId = t.get(image.id);
            if (imgId != null) {
                dto.getImageDtoList().add(
                        new ImageDto(
                                imgId,
                                t.get(image.originalImageName),
                                t.get(image.storedImageName),
                                t.get(image.imageExtension)
                        )
                );
            }
        }

        return ((List<FeedDto>) new ArrayList<>(map.values())).get(0);
    }

    @Override
    public Long updateFeedStatusById(Long id) {
        return jpaQueryFactory.update(feed)
                .set(feed.status, Status.INACTIVE)
                .where(
                        feed.id.eq(id)
                )
                .execute();
    }

    // User 가 작성한 피드 목록 조회. (피드 이미지는 첫번째 이미지만 불러오기)
    @Override
    public List<MyFeedDto> findFeedByUser(User user) {
        return jpaQueryFactory
                .select(Projections.bean(MyFeedDto.class,
                        feed.id.as("feedId"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(image.storedImageName)
                                        .from(image)
                                        .where(image.id.eq(
                                                JPAExpressions
                                                        .select(image.id.min())
                                                        .from(image)
                                                        .where(image.feed.eq(feed))
                                        ))
                                , "imgName"
                        )))
                .from(feed)
                .where(feed.user.id.eq(user.getId()).and(feed.status.eq(Status.ACTIVE)))
                .orderBy(feed.createdAt.desc()) // 최신순으로
                .fetch()
                .stream().toList();
    }

    @Override
    public List<Feed> findFeeds(Long lastFeedId, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(feed)
                .leftJoin(feed.user).fetchJoin()
                .leftJoin(feed.images).fetchJoin()
                .where(
                        feed.status.eq(Status.ACTIVE).and(
                                (lastFeedId != null ? feed.id.lt(lastFeedId) : null)
                        )

                ) // if(닉네임 != null)
                .orderBy(feed.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Feed> findFeedsByNickname(String nickname, Long lastFeedId, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(feed)
                .leftJoin(feed.user).fetchJoin()
                .leftJoin(feed.images).fetchJoin()
                .where(
                        feed.status.eq(Status.ACTIVE).and(
                                feed.user.nickName.eq(nickname).and(
                                        (lastFeedId != null ? feed.id.lt(lastFeedId) : null)
                                )
                        )
                )
                .orderBy(feed.id.desc())
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

 
