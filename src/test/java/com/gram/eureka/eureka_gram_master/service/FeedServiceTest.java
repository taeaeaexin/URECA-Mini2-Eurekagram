package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.Image;
import com.gram.eureka.eureka_gram_master.repository.*;
import com.gram.eureka.eureka_gram_master.service.impl.FeedServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Mockito 테스트
@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    // Mock 처리
    @Mock FeedRepository feedRepository;
    @Mock FeedViewRepository feedViewRepository;
    @Mock CommentRepository commentRepository;
    @Mock UserRepository userRepository;
    @Mock ImageRepository imageRepository;

    @InjectMocks FeedServiceImpl feedService;

    @Test
    void detailFeedTest() {
        Long feedId = 1L;
        FeedDto dto = new FeedDto(feedId, "test content", null, List.of(), false);

        when(feedRepository.findFeedInfoById(feedId)).thenReturn(dto);
        when(feedViewRepository.getFeedViewCount(feedId)).thenReturn(7L);

        FeedDto result = feedService.detailFeed(feedId);

        assertThat(result.getFeedId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("test content");
        assertThat(result.getFeedViewCount()).isEqualTo(7L);
    }

    @Test
    void updateFeedTest() {
        when(feedRepository.updateFeedStatusById(2L)).thenReturn(1L);

        FeedResponseDto result = feedService.updateFeed(2L);

        assertThat(result.getFeedCount()).isEqualTo(1L);
    }

    @Test
    void getFeedsTest() {
        Feed feed = Feed.builder()
                .id(1L)
                .content("test content")
                .images(List.of(Image.builder().storedImageName("test.jpg").build()))
                .build();

        List<Feed> feedList = List.of(feed);
        when(feedRepository.findFeeds(null, PageRequest.of(0, 10))).thenReturn(feedList);

        Object[] commentRow = new Object[]{1L, 3L};
        Object[] viewRow = new Object[]{1L, 10L};

        List<Object[]> commentData = new ArrayList<>();
        commentData.add(new Object[]{1L, 3L});

        List<Object[]> viewData = new ArrayList<>();
        viewData.add(new Object[]{1L, 10L});

        when(commentRepository.countByFeedId(List.of(1L))).thenReturn(commentData);
        when(feedViewRepository.countByFeedId(List.of(1L))).thenReturn(viewData);

        List<FeedResponseDto> results = feedService.getFeeds(null, 10, null);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCommentCount()).isEqualTo(3L);
        assertThat(results.get(0).getViewCount()).isEqualTo(10L);
        assertThat(results.get(0).getImages().get(0)).isEqualTo("/images/test.jpg");
    }
}