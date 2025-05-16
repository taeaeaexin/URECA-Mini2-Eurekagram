package com.gram.eureka.eureka_gram_master.controller;

import com.gram.eureka.eureka_gram_master.dto.FeedResponseDto;
import com.gram.eureka.eureka_gram_master.dto.MyFeedsResponseDto;
import com.gram.eureka.eureka_gram_master.dto.query.FeedDto;
import com.gram.eureka.eureka_gram_master.service.FeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
@AutoConfigureMockMvc

// Security
@WithMockUser
public class FeedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedService feedService;

    @Test
    void getFeedsTest() throws Exception {
        FeedResponseDto dto = new FeedResponseDto();
        dto.setFeedId(1L);
        dto.setContent("test content");

        List<FeedResponseDto> mockList = List.of(dto);

        when(feedService.getFeeds(null, 10, null)).thenReturn(mockList);

        mockMvc.perform(get("/feeds")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].content").value("test content"));
    }

    @Test
    void detailFeedTest() throws Exception {
//        Long feedId = 1L;
//
//        FeedDto mockDto = new FeedDto(feedId, "detail content", null, List.of(),false);
//
//        when(feedService.detailFeed(feedId)).thenReturn(mockDto);
//
//        mockMvc.perform(get("/feeds/" + feedId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.statusCode", is(200)))
//                .andExpect(jsonPath("$.message", is("success")))
//                .andExpect(jsonPath("$.data.feedId", is(feedId.intValue())))
//                .andExpect(jsonPath("$.data.content", is("detail content")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // 관리자 권한 부여
    void deleteFeedTest() throws Exception {
        Long feedId = 2L;
        FeedResponseDto mockResponse = new FeedResponseDto();
        mockResponse.setFeedId(feedId);
        mockResponse.setContent("삭제 완료");

        when(feedService.updateFeed(feedId)).thenReturn(mockResponse);

        mockMvc.perform(delete("/feeds/{id}", feedId).with(csrf())) // CSRF 토큰
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.feedId", is(feedId.intValue())))
                .andExpect(jsonPath("$.data.content", is("삭제 완료")));
    }

    @Test
    void myFeedTest() throws Exception {
        MyFeedsResponseDto mockDto = new MyFeedsResponseDto();
        mockDto.setCount(5);

        when(feedService.myFeed()).thenReturn(mockDto);

        mockMvc.perform(get("/feeds/my-feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("내 피드 조회 성공")))
                .andExpect(jsonPath("$.data.count", is(5)));
    }
}