package com.gram.eureka.eureka_gram_user.entity;

import com.gram.eureka.eureka_gram_user.dto.UserRequestDto;
import com.gram.eureka.eureka_gram_user.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가 값 (Auto Increment)
    private String userName; // 사용자 이름
    private String email; // 로그인시 사용할 이메일 (중복 체크 필요)
    private String password; // 로그인시 사용할 비밀번호
    private String nickName; // 로그인 이후 사용자 화면에 표시될 닉네임
    private String phoneNumber; // 휴대폰 번호
    @Enumerated(EnumType.STRING)
    private Batch batch; // 기수
    @Enumerated(EnumType.STRING)
    private Track track; // 구분
    @Enumerated(EnumType.STRING)
    private Mode mode; // 방식
    @Enumerated(EnumType.STRING)
    private Status status; // 상태값 (인증 전, 사용가능, 사용불가)
    @Enumerated(EnumType.STRING)
    private Role role; // 역할


    public static User of(UserRequestDto userRequestDto) {
        return User.builder()
                .userName(userRequestDto.getUserName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .nickName(userRequestDto.getNickName())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .batch(userRequestDto.getBatch())
                .track(userRequestDto.getTrack())
                .mode(userRequestDto.getMode())
                .status(Status.PENDING) // 기본값은 PENDING 으로 설정
                .role(Role.ROLE_USER) // 기본값은 ROLE_USER 으로 설정
                .build();
    }
}
