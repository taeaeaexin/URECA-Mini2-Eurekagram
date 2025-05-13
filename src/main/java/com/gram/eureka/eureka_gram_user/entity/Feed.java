package com.gram.eureka.eureka_gram_user.entity;

import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content; // 피드 내용

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status; // 상태 관리

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();
}
