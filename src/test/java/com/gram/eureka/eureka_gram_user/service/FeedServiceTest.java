package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedsResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.Image;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.FeedRepository;
import com.gram.eureka.eureka_gram_user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private Long testFeedId;

    @BeforeEach
    public void SetUp(){
        // test 사용자 생성
        User user = User.builder()
                .email("test@naver.com")
                .nickName("test")
                .password("testpw")
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        // 인증된 사용자 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test@naver.com", null)
        );

        // test 피드 생성
        Feed feed1 = Feed.builder()
                .content("test content 1")
                .user(user)
                .status(Status.ACTIVE)
                .build();
        Feed feed2 = Feed.builder()
                .content("test content 2")
                .user(user)
                .status(Status.ACTIVE)
                .build();
        feedRepository.save(feed1);
        feedRepository.save(feed2);
        testFeedId = feed1.getId();
    }

    @AfterEach
    void clearSet(){
        SecurityContextHolder.clearContext();
    }



    @Test
    @Transactional
    @DisplayName("testCreateFeed() : 피드 생성 메소드 테스트.")
    public void testCreateFeed() {
        // given
        List<MultipartFile> images = List.of(new MockMultipartFile(
                "images",
                "image.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        ));

        FeedRequestDto feedRequestDto = new FeedRequestDto();
        feedRequestDto.setContent("test content");
        feedRequestDto.setImages(images);

        // when
        FeedResponseDto feedResponseDto = feedService.createFeed(feedRequestDto);

        // then
        assertNotNull(feedResponseDto.getFeedId());
    }


    @Test
    @Transactional
    @DisplayName("testDetailFeed() : 피드 상세 조회 메소드 테스트.")
    public void testDetailFeed() {
        // given : testFeedId
        // when
        FeedDto feedDto = feedService.detailFeed(testFeedId);

        // 변경사항 즉시 반영, 1차캐시 비우기
        em.flush();
        em.clear();

        // then
        // 첫 조회 (조회수 증가)
        assertNotNull(feedDto);
        assertEquals(testFeedId, feedDto.getFeedId());
        assertEquals(1L, feedDto.getFeedViewCount());

        // 두번째 조회 (조회수 증가 X)
        feedDto = feedService.detailFeed(testFeedId);
        assertEquals(1L, feedDto.getFeedViewCount());

    }


    @Test
    @Transactional
    @DisplayName("testUpdateFeed_Delete() : 피드 삭제 메소드 테스트.")
    public void testUpdateFeed_Delete() {
        // given : testFeedId
        // when
        FeedResponseDto feedResponseDto = feedService.updateFeed(testFeedId);

        em.flush();
        em.clear(); // 1차캐시 비우기. 이거 해야 아래에서 findById할 때 DB에서 최신값 가져옴.

        // then
        assertEquals(1L, feedResponseDto.getFeedCount()); // 삭제됐다면 1
        assertEquals(Status.INACTIVE, feedRepository.findById(testFeedId).get().getStatus());
    }


    @Test
    @Transactional
    @DisplayName("testMyFeed() : 내 피드 조회 메소드 테스트.")
    public void testMyFeed() {
        // when
        MyFeedsResponseDto myFeedsResponseDto = feedService.myFeed();

        // then
        assertNotNull(myFeedsResponseDto);

        // 피드 id를 잘 받아왔는지
        myFeedsResponseDto.getFeeds().forEach(feed -> {
            assertNotNull(feed.getFeedId());
        });

        // 받아온 피드 개수가 맞는지
        assertEquals(2L, myFeedsResponseDto.getCount());
    }

    // 닉네임 존재/미존재(ParameterizedTest?). 피드 존재/미존재(deleteAll) 분리.
    // 댓글 테스트데이터 만들기
    @Test
    @Transactional
    @DisplayName("#미완. testgetFeeds() : 전체 피드 목록 조회 메소드 테스트.")
    public void testGetFeeds_total() {
        // given
        Long lastFeedId = 1L;
        int size = 10;
        String nickname = "";

        // when


        // then

    }


    @Test
    @Transactional
    @DisplayName("#미완. testUpdateFeed_Modify() : 피드 수정 메소드 테스트.")
    public void testUpdateFeed_Modify() {
        // given
        List<MultipartFile> images = List.of(new MockMultipartFile(
                "images",
                "image.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        ));

        FeedRequestDto feedRequestDto = new FeedRequestDto();
        feedRequestDto.setContent("test update content");
        feedRequestDto.setImages(images);

        // when

        // then
    }

}
