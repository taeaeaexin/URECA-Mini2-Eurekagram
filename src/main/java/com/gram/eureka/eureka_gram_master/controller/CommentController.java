package com.gram.eureka.eureka_gram_master.controller;

import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_master.dto.CommentResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.CommentDto;
import com.gram.eureka.eureka_gram_master.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    @ResponseBody
    public BaseResponseDto<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(commentRequestDto);
    }

    @PostMapping("/all")
    @ResponseBody
    public BaseResponseDto<List<CommentDto>> getComment(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.getComment(commentRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public BaseResponseDto<CommentResponseDto> removeComment(@PathVariable Long id) {
        return commentService.removeComment(id);
    }
}
