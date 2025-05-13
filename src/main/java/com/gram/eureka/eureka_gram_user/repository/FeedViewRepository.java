package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.FeedView;
import com.gram.eureka.eureka_gram_user.repository.custom.FeedViewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface FeedViewRepository extends JpaRepository<FeedView, Long>, FeedViewRepositoryCustom {
    @Query("" +
            "SELECT fv.feed.id, COUNT(fv) " +
            "FROM FeedView fv " +
            "WHERE fv.feed.id IN :feedIds " +
            "GROUP BY fv.feed.id"
    )
    List<Object[]> countByFeedId(@Param("feedIds") List<Long> feedIds);
}
