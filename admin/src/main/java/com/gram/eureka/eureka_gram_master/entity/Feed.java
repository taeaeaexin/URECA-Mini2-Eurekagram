package com.gram.eureka.eureka_gram_master.entity;

import com.gram.eureka.eureka_gram_master.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content; // 피드 내용

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status; // 상태 관리

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();
}
