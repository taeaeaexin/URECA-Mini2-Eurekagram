package com.gram.eureka.eureka_gram_master.repository;

import com.gram.eureka.eureka_gram_master.entity.Comment;
import com.gram.eureka.eureka_gram_master.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query(
            "SELECT c.feed.id, COUNT(c) " +
                    "FROM Comment c " +
                    "WHERE c.feed.id IN :feedIds " +
                    "AND c.status = 'ACTIVE' " +
                    "GROUP BY c.feed.id"
    )
    List<Object[]> countByFeedId(@Param("feedIds") List<Long> feedIds);
}
