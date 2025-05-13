package com.gram.eureka.eureka_gram_master.dto;

// 사용자 관리용 dto.
import com.gram.eureka.eureka_gram_master.entity.enums.Batch;
import com.gram.eureka.eureka_gram_master.entity.enums.Mode;
import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import com.gram.eureka.eureka_gram_master.entity.enums.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserManagementDto {
    private Long userId;
    private String userName; // 사용자 이름
    private String nickName; // 로그인 이후 사용자 화면에 표시될 닉네임
    private String phoneNumber; // 휴대폰 번호
    private Batch batch; // 기수
    private Track track; // 구분
    private Mode mode; // 방식
    private LocalDateTime createdAt; // 가입일
    private Status status; // 상태
}
