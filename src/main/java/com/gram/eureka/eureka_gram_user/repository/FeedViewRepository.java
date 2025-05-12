package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.FeedView;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedViewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedViewRepository extends JpaRepository<FeedView, Long>, FeedViewRepositoryCustom {
}
