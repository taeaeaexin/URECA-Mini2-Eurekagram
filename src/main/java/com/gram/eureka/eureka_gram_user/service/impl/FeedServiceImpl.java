package com.gram.eureka.eureka_gram_user.service.impl;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;

import com.gram.eureka.eureka_gram_user.dto.*;

import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.Image;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.*;
import com.gram.eureka.eureka_gram_user.service.FeedService;
import com.gram.eureka.eureka_gram_user.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

@Transactional
@Slf4j
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

}
