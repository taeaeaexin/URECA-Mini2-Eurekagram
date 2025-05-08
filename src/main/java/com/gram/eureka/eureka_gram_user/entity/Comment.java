package com.gram.eureka.eureka_gram_user.entity;

import jakarta.persistence.*;

@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Feed feed;

    @ManyToOne
    private User user;

    private String content; // 댓글 내용
}
