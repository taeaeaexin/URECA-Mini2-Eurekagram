package com.gram.eureka.eureka_gram_user.entity;

import com.gram.eureka.eureka_gram_user.entity.enums.Status;
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
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // 피드 제목
    private String content; // 피드 내용

    @ManyToOne
    private User user;

    private Status status; // 상태 관리
}
