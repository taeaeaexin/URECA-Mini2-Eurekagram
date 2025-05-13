package com.gram.eureka.eureka_gram_master.entity;

import com.gram.eureka.eureka_gram_master.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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

}
