package com.gram.eureka.eureka_gram_master.controller;


import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.UserManagementDto;
import com.gram.eureka.eureka_gram_master.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    // 사용자 목록 조회
    @GetMapping
    public BaseResponseDto<List<UserManagementDto>> userList(@RequestParam(required = false, defaultValue = "ALL") String status,
                                                             @RequestParam(required = false) String nickname) {

        try {
            List<UserManagementDto> userManagementDtoList = userService.userList(status, nickname);

            return BaseResponseDto.<List<UserManagementDto>>builder()
                    .statusCode(200)
                    .message("사용자 리스트 조회 성공")
                    .data(userManagementDtoList)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();

            return BaseResponseDto.<List<UserManagementDto>>builder()
                    .statusCode(500)
                    .message("사용자 리스트 조회 실패")
                    .data(null)
                    .build();
        }
    }

    // 사용자 상태 수정
    @PutMapping("/{userId}")
    public BaseResponseDto updateUser(@PathVariable Long userId,
                                      @RequestBody Map<String, String> statusMap) {

        try{
            String status = statusMap.get("status");
            userService.updateUser(userId, status);

            return BaseResponseDto.builder()
                    .statusCode(200)
                    .message("수정 성공")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());

            return BaseResponseDto.builder()
                    .statusCode(500)
                    .message("수정 실패")
                    .build();
        }
    }

}
