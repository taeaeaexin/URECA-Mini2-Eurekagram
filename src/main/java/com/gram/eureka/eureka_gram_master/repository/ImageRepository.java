package com.gram.eureka.eureka_gram_master.repository;

import com.gram.eureka.eureka_gram_master.entity.Image;
import com.gram.eureka.eureka_gram_master.repository.custom.ImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
