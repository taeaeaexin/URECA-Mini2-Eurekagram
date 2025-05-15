package com.gram.eureka.eureka_gram_master.service;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.CommentResponseDto;
import com.gram.eureka.eureka_gram_master.repository.CommentRepository;
import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import com.gram.eureka.eureka_gram_master.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void removeComment() {
        Long commentId = 1L;
        when(commentRepository.updateStatusById(commentId)).thenReturn(commentId);

        BaseResponseDto<CommentResponseDto> response = commentService.removeComment(commentId);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getData().getCommentId()).isEqualTo(commentId);
    }
}
