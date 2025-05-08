package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
