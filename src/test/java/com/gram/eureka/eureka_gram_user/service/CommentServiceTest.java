package com.gram.eureka.eureka_gram_user.service;

import com.gram.eureka.eureka_gram_user.dto.*;
import com.gram.eureka.eureka_gram_user.dto.query.CommentDto;
import com.gram.eureka.eureka_gram_user.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_user.entity.Comment;
import com.gram.eureka.eureka_gram_user.entity.Feed;
import com.gram.eureka.eureka_gram_user.entity.User;
import com.gram.eureka.eureka_gram_user.entity.enums.Role;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.CommentRepository;
import com.gram.eureka.eureka_gram_user.repository.FeedRepository;
import com.gram.eureka.eureka_gram_user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    private Long testFeedId;
    private Long testCommentId;

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
        feedRepository.save(feed1);
        testFeedId = feed1.getId();

        // test 댓글 생성
        Comment comment1 = Comment.builder()
                .content("test comment 1")
                .feed(feed1)
                .user(user)
                .status(Status.ACTIVE)
                .build();
        commentRepository.save(comment1);
        testCommentId = comment1.getId();
    }

    @AfterEach
    void clearSet(){
        SecurityContextHolder.clearContext();
    }



    @Test
    @Transactional
    @DisplayName("testCreateComment() : 댓글 생성 메소드 테스트.")
    public void testCreateComment() {
        // given
        User user = userRepository.findByEmail("test@naver.com").orElseThrow();

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setFeedId(testFeedId);
        commentRequestDto.setContent("test comment");

        // when
        BaseResponseDto<CommentResponseDto> response = commentService.createComment(commentRequestDto);

        // then
        // 응답 잘 들어왔는지
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("댓글이 성공적으로 등록되었습니다.", response.getMessage());
        assertNotNull(response.getData().getCommentId());

        // 댓글의 feed, user값이 정확하게 들어갔는지
        Long commentId = response.getData().getCommentId();
        assertEquals(testFeedId,
                commentRepository.findById(commentId).get()
                        .getFeed().getId());
        assertEquals(user.getId(),
                commentRepository.findById(commentId).get()
                        .getUser().getId());

    }


    @Test
    @Transactional
    @DisplayName("testGetComment() : 댓글 조회 메소드 테스트.")
    public void testGetComment() {
        // given
        User user = userRepository.findByEmail("test@naver.com").orElseThrow();

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setFeedId(testFeedId);
        commentRequestDto.setContent("test comment");

        // when
        BaseResponseDto<List<CommentDto>> response = commentService.getComment(commentRequestDto);

        // then
        // 응답 잘 들어왔는지
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("댓글이 성공적으로 조회되었습니다.", response.getMessage());
        assertEquals(user.getId(), commentRequestDto.getUserId());

    }


    @Test
    @Transactional
    @DisplayName("testRemoveComment() : 댓글 삭제 메소드 테스트.")
    public void testRemoveComment() {
        // given: commentId
        // when
        BaseResponseDto<CommentResponseDto> response = commentService.removeComment(testCommentId);

        // then
        // 응답 잘 들어왔는지
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("댓글이 성공적으로 삭제 되었습니다.", response.getMessage());
        assertEquals(1L, response.getData().getCommentId());
    }

}
