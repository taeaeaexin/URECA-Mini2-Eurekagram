package com.gram.eureka.eureka_gram_user.repository.custom;

import com.gram.eureka.eureka_gram_user.entity.Comment;
import com.gram.eureka.eureka_gram_user.entity.Feed;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findCommentByFeed(Feed feed);
}
