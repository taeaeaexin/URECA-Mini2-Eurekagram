package com.gram.eureka.eureka_gram_user.dto;

import com.gram.eureka.eureka_gram_user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String email;
    private String userName;
    private String nickName;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .build();
    }
}
