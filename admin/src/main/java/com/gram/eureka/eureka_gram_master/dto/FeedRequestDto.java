package com.gram.eureka.eureka_gram_master.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedRequestDto {
    private String content;
    private List<MultipartFile> images;
}
