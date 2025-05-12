package com.gram.eureka.eureka_gram_user.repository;

import com.gram.eureka.eureka_gram_user.entity.Image;
import com.gram.eureka.eureka_gram_user.repository.custom.ImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
