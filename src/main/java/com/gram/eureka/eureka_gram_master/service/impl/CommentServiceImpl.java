package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_master.dto.CommentResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.CommentDto;
import com.gram.eureka.eureka_gram_master.entity.Comment;
import com.gram.eureka.eureka_gram_master.entity.Feed;
import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.repository.CommentRepository;
import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public BaseResponseDto<CommentResponseDto> createComment(CommentRequestDto commentRequestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 기본적으로 username 반환
        User findUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Feed feed = feedRepository.findById(commentRequestDto.getFeedId()).orElseThrow();
        User user = userRepository.findById(findUser.getId()).orElseThrow();
        Comment comment = Comment.builder()
                .feed(feed)
                .user(user)
                .content(commentRequestDto.getContent())
                .status(Status.ACTIVE)
                .build();

        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setCommentId(saveComment.getId());

        return BaseResponseDto.<CommentResponseDto>builder()
                .statusCode(200)
                .message("댓글이 성공적으로 등록되었습니다.")
                .data(commentResponseDto)
                .build();
    }

    @Override
    public BaseResponseDto<List<CommentDto>> getComment(CommentRequestDto commentRequestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 기본적으로 username 반환
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        commentRequestDto.setUserId(user.getId());

        List<CommentDto> commentDtoList = commentRepository.findComments(commentRequestDto);
        return BaseResponseDto.<List<CommentDto>>builder()
                .statusCode(200)
                .message("댓글이 성공적으로 조회되었습니다.")
                .data(commentDtoList)
                .build();
    }

    @Override
    public BaseResponseDto<CommentResponseDto> removeComment(Long id) {
        Long updateCommentId = commentRepository.updateStatusById(id);
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setCommentId(updateCommentId);
        return BaseResponseDto.<CommentResponseDto>builder()
                .statusCode(200)
                .message("댓글이 성공적으로 삭제 되었습니다.")
                .data(commentResponseDto)
                .build();
    }
}
