package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.Comment;
import com.gram.eureka.eureka_gram_user.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query(
            "SELECT c.feed.id, COUNT(c) " +
                    "FROM Comment c " +
                    "WHERE c.feed.id IN :feedIds " +
                    "GROUP BY c.feed.id"
    )
    List<Object[]> countByFeedId(@Param("feedIds") List<Long> feedIds);
}
