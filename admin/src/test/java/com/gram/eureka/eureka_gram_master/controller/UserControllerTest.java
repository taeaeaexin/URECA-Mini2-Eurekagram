package com.gram.eureka.eureka_gram_master.controller;

import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.entity.enums.Batch;
import com.gram.eureka.eureka_gram_master.entity.enums.Mode;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.entity.enums.Track;
import com.gram.eureka.eureka_gram_master.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)

@WithMockUser(roles = "ADMIN") // 클래스에 ADMIN
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void userListTest() throws Exception {
        UserManagementDto test_user = new UserManagementDto().builder()
                .userId(1L)
                .nickName("User")
                .status(Status.ACTIVE)
                .track(Track.BACKEND)
                .mode(Mode.OFFLINE)
                .batch(Batch.SECOND)
                .build();

        Page<UserManagementDto> page = new PageImpl<>(List.of(test_user), PageRequest.of(0, 10), 1);

        Mockito.when(userService.userList(eq("ALL"), eq(null), any()))
                .thenReturn(page);

        mockMvc.perform(get("/users")
                        .param("status", "ALL")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("사용자 리스트 조회 성공")))
                .andExpect(jsonPath("$.data.content[0].nickName", is("User")));
    }

    @Test
    void updateUserTest() throws Exception {
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"INACTIVE\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }
}

