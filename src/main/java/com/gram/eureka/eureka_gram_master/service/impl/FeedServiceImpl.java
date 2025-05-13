package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_master.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_master.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_master.dto.MyFeedsResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.Image;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.*;
import com.gram.eureka.eureka_gram_master.service.FeedService;
import com.gram.eureka.eureka_gram_master.util.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageUtil imageUtil;
    private final CommentRepository commentRepository;
    private final FeedViewRepository feedViewRepository;

    @Override
    public FeedResponseDto createFeed(FeedRequestDto feedRequestDto) {
        // User 엔티티 생성 > Jwt 토큰으로부터 정보 추출 및 findByEmail 실행
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 기본적으로 username 반환
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        // 피드 등록을 위한 feed 엔티티 생성
        Feed feed = Feed.builder()
                .content(feedRequestDto.getContent())
                .user(user)
                .status(Status.ACTIVE)
                .build();
        // feed 등록
        feedRepository.save(feed);

        // feed 매핑되는 이미지 등록
        List<Image> imageList = new ArrayList<>();
        for (MultipartFile mpFile : feedRequestDto.getImages()) {
            String fullName = mpFile.getOriginalFilename();
            String[] splitFullName = fullName.split("\\.");
            String extension = splitFullName[splitFullName.length - 1];

            Image image = Image.builder()
                    .feed(feed)
                    .originalImageName(fullName)
                    .storedImageName(imageUtil.saveImageToDisk(mpFile))
                    .imageExtension(extension)
                    .build();

            imageList.add(image);
        }

        imageRepository.saveAll(imageList);

        FeedResponseDto feedResponseDto = new FeedResponseDto();
        feedResponseDto.setFeedId(feed.getId());
        return feedResponseDto;
    }

    @Override
    public FeedDto detailFeed(Long id) {
        FeedDto feedDto = feedRepository.findFeedInfoById(id);
        Long feedViewCount = feedViewRepository.getFeedViewCount(id);
        feedDto.setFeedViewCount(feedViewCount);
        return feedDto;
    }

    @Override
    public FeedResponseDto updateFeed(Long id) {
        log.info("service detailFeed id : {}", id);
        Long updateFeedCount = feedRepository.updateFeedStatusById(id);
        FeedResponseDto feedResponseDto = new FeedResponseDto();
        feedResponseDto.setFeedCount(updateFeedCount);
        return feedResponseDto;
    }

    public MyFeedsResponseDto myFeed() {
        // User 엔티티 생성 > Jwt 토큰으로부터 정보 추출 및 findByEmail 실행
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 기본적으로 username 반환
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        // 내 피드 목록 조회 (feed_id, img name 리스트)
        List<MyFeedDto> feeds = feedRepository.findFeedByUser(user);

        // 내 피드 개수
        int count = feeds.size();

        return MyFeedsResponseDto.builder()
                .feeds(feeds)
                .count(count)
                .build();
    }

    @Override
    public List<FeedResponseDto> getFeeds(Long lastFeedId, int size, String nickname) {
        List<Feed> feeds;

        if (!StringUtils.hasText(nickname)) {
            feeds = feedRepository.findFeeds(lastFeedId, PageRequest.of(0, size));
        } else {
            feeds = feedRepository.findFeedsByNickname(nickname, lastFeedId, PageRequest.of(0, size));
        }

        if (feeds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> feedIds = feeds.stream()
                .map(Feed::getId)
                .toList(); // Java 16+ or use .collect(Collectors.toList())

        Map<Long, Long> commentCounts = toCountMap(commentRepository.countByFeedId(feedIds));
        Map<Long, Long> viewCounts = toCountMap(feedViewRepository.countByFeedId(feedIds));

        return feeds.stream().map(feed -> {
            FeedResponseDto dto = new FeedResponseDto();
            dto.setFeedId(feed.getId());
            dto.setContent(feed.getContent());
            dto.setImages(
                    feed.getImages().stream()
                            .map(image -> "/images/" + image.getStoredImageName())
                            .toList()
            );
            dto.setCommentCount(commentCounts.getOrDefault(feed.getId(), 0L));
            dto.setViewCount(viewCounts.getOrDefault(feed.getId(), 0L));
            return dto;
        }).toList();
    }

    private Map<Long, Long> toCountMap(List<Object[]> rows) {
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            Long feedId = ((Number) row[0]).longValue();
            Long count = ((Number) row[1]).longValue();
            map.put(feedId, count);
        }
        return map;
    }
}
