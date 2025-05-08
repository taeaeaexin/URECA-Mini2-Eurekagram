package com.gram.eureka.eureka_gram_user.entity;

import jakarta.persistence.*;

@Entity
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // 피드 제목
    private String content; // 피드 내용

    @ManyToOne
    private User user;

}
