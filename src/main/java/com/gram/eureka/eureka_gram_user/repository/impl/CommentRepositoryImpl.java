package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.entity.Comment;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gram.eureka.eureka_gram_user.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findCommentByFeed(Feed feed) {
        return jpaQueryFactory.select(comment)
                .from(comment)
                .where(
                        comment.feed.eq(feed)
                                .and(comment.status.eq(Status.ACTIVE))
                )
                .fetch();
    }
}
