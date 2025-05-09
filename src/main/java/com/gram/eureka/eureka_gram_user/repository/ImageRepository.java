package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
