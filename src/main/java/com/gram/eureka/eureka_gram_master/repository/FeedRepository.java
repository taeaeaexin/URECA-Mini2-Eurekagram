package com.gram.eureka.eureka_gram_master.repository;

import com.gram.eureka.eureka_gram_master.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByUserId(Long userId);
}
