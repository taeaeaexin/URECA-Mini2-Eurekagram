package com.gram.eureka.eureka_gram_master.dto;

import com.gram.eureka.eureka_gram_master.entity.enums.Batch;
import com.gram.eureka.eureka_gram_master.entity.enums.Mode;
import com.gram.eureka.eureka_gram_master.entity.enums.Track;
import lombok.Data;

@Data
public class UserRequestDto {
    private String userName; // 사용자 이름
    private String email; // 로그인시 사용할 이메일 (중복 체크 필요)
    private String password; // 로그인시 사용할 비밀번호
    private String nickName; // 로그인 이후 사용자 화면에 표시될 닉네임
    private String phoneNumber; // 휴대폰 번호
    private Batch batch; // 기수
    private Track track; // 구분
    private Mode mode; // 방식
}

/**
 * {
 *   "userName": "홍길동",
 *   "email": "hong@example.com",
 *   "password": "securePassword123!",
 *   "nickName": "길동이",
 *   "batch": "FIRST",
 *   "track": "BACKEND",
 *   "mode": "ONLINE"
 * }
 */