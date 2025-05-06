package com.gram.eureka.eureka_gram_user.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageUtil {
    public String saveImageToDisk(MultipartFile image) {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/" + fileName;

        try {
            image.transferTo(new File(savePath));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        return "/images/" + fileName; // 프론트에 제공할 경로
    }
}
