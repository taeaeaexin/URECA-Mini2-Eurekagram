package com.gram.eureka.eureka_gram_user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedRequestDto {
    private Long id;
    private String content;
    private List<MultipartFile> images;
    private List<Long> remainImageIds;
}
