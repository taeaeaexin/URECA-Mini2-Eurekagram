package com.gram.eureka.eureka_gram_master.repository;

import com.gram.eureka.eureka_gram_master.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
