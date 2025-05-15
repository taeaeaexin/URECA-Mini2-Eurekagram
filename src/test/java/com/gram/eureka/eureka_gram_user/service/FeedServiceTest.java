package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.FeedRequestDto;
import com.gram.eureka.eureka_gram_user.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_user.dto.MyFeedsResponseDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.entity.*;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.*;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedViewRepository feedViewRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EntityManager em;

    private Long testFeedId, testLastFeedId;

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
        User user2 = User.builder()
                .email("test2@naver.com")
                .nickName("test2")
                .password("testpw")
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        userRepository.save(user2);

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
                .user(user2)
                .status(Status.ACTIVE)
                .build();
        Feed feed3 = Feed.builder()
                .content("test content 3")
                .user(user)
                .status(Status.ACTIVE)
                .build();
        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        testFeedId = feed1.getId();
        testLastFeedId = feed3.getId()+1;


        // test image 생성
        Image img1 = Image.builder()
                .storedImageName("feed1img.jpg")
                .feed(feed1)
                .build();
        Image img2 = Image.builder()
                .storedImageName("feed2img.jpg")
                .feed(feed2)
                .build();
        imageRepository.save(img1);
        imageRepository.save(img2);

        // 댓글 수 및 조회수 데이터 추가
        Comment comment1 = Comment.builder()
                .feed(feed1)
                .content("댓글1")
                .status(Status.ACTIVE)
                .build();
        Comment comment2 = Comment.builder()
                .feed(feed1)
                .content("댓글2")
                .status(Status.ACTIVE)
                .build();
        Comment comment3 = Comment.builder()
                .feed(feed2)
                .content("댓글3")
                .status(Status.ACTIVE)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        FeedView feedView = FeedView.builder()
                .feed(feed1)
                .user(user)
                .build();
        FeedView feedView2 = FeedView.builder()
                .feed(feed1)
                .user(user2)
                .build();
        feedViewRepository.save(feedView);
        feedViewRepository.save(feedView2);

        em.flush();
        em.clear();
    }

    @AfterEach
    void clearSet(){
        SecurityContextHolder.clearContext();
        feedViewRepository.deleteAll();
        commentRepository.deleteAll();
        imageRepository.deleteAll();
        feedRepository.deleteAll();
        userRepository.deleteAll();
        em.flush();
        em.clear();
    }

    @Order(1)
    @Test
    @Transactional
    @DisplayName("testDetailFeed() : 피드 상세 조회 메소드 테스트.")
    public void testDetailFeed() {
        // given
        // when 첫 조회 (조회수 증가)
        FeedDto feedDto = feedService.detailFeed(testFeedId);

        em.flush();
        em.clear();

        // then:
        assertNotNull(feedDto);
        assertEquals(testFeedId, feedDto.getFeedId());
        assertEquals(1L, feedDto.getFeedViewCount());

        // when 두 번째 조회 (조회수 증가X)
        FeedDto feedDto2 = feedService.detailFeed(testFeedId);
        em.flush();

        // then
        assertEquals(1L, feedDto2.getFeedViewCount());

    }

    @Order(2)
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


    @Order(3)
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


    @Order(4)
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


    @Order(5)
    @Test
    @Transactional
    @DisplayName("#testgetFeeds() : 전체 피드 목록 조회 메소드 테스트.")
    public void testGetFeeds_total() {
        // given
        int size = 3;
        String nickname = "";

        // when
        List<FeedResponseDto> response = feedService.getFeeds(testLastFeedId, size, nickname);

        // then
        assertEquals(3, response.size());
        FeedResponseDto feedResponseDto =  response.stream()
                .filter(dto -> dto.getFeedId().equals(testFeedId))
                .findFirst()
                .orElseThrow();
        assertEquals(2L, feedResponseDto.getCommentCount());
        assertEquals(2L, feedResponseDto.getViewCount());


        // when : 피드 없는 경우
        commentRepository.deleteAll();
        imageRepository.deleteAll();
        feedViewRepository.deleteAll();
        feedRepository.deleteAll();
        em.flush();

        List<FeedResponseDto> responseEmpty = feedService.getFeeds(null, 10, nickname);

        // then
        assertNotNull(responseEmpty);
        assertTrue(responseEmpty.isEmpty());
    }

    @Order(6)
    @Test
    @Transactional
    @DisplayName("#testGetFeeds_nickname() : 닉네임 검색 피드 목록 조회 메소드 테스트.")
    public void testGetFeeds_nickname() {
        // given
        int size = 3;
        String nickname = "test";

        // when
        List<FeedResponseDto> response = feedService.getFeeds(testLastFeedId, size, nickname);

        // then
        assertEquals(2, response.size());
        FeedResponseDto feedResponseDto =  response.stream()
                .filter(dto -> dto.getFeedId().equals(testFeedId))
                .findFirst()
                .orElseThrow();
        assertEquals(2L, feedResponseDto.getCommentCount());
        assertEquals(2L, feedResponseDto.getViewCount());


        // when : 피드 없는 경우
        commentRepository.deleteAll();
        imageRepository.deleteAll();
        feedViewRepository.deleteAll();
        feedRepository.deleteAll();
        em.flush();

        List<FeedResponseDto> responseEmpty = feedService.getFeeds(null, 10, nickname);

        // then
        assertNotNull(responseEmpty);
        assertTrue(responseEmpty.isEmpty());
    }


    @Order(7)
    @Test
    @Transactional
    @DisplayName("testUpdateFeed_Modify() : 피드 수정 메소드 테스트.")
    public void testUpdateFeed_Modify() {
        // given
        List<MultipartFile> images = List.of(new MockMultipartFile(
                "images",
                "image.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        ));
        FeedRequestDto feedRequestDto = new FeedRequestDto();
        feedRequestDto.setId(testFeedId);
        feedRequestDto.setContent("updated content");
        feedRequestDto.setImages(images);
        feedRequestDto.setRemainImageIds(Collections.emptyList());

        // when
        FeedResponseDto response = feedService.updateFeed(feedRequestDto);

        em.flush();
        em.clear();

        // then
        Feed updatedFeed = feedRepository.findById(testFeedId).orElseThrow();
        assertEquals("updated content", updatedFeed.getContent());

        List<Image> updatedImages = updatedFeed.getImages();
        assertNotNull(updatedImages);
        assertEquals(1, updatedImages.size());
        assertEquals("jpg", updatedImages.get(0).getImageExtension());

        assertEquals(testFeedId, response.getFeedId());
    }

}
