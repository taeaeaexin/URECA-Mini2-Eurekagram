package com.gram.eureka.eureka_gram_user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/page")
public class PageController {
    /**
     * page 이동시에 해당 API 를 반드시 호출하여 jwt 인증을 처리해야함
     */
    @PostMapping("/authenticate")
    public ResponseEntity<String> pageAuthenticate(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        return ResponseEntity.ok(email);
    }


    /**
     * PageController 를 통하여 페이지 이동 처리
     */
    @GetMapping("/main")
    public String moveMainPage() {
        return "/html/main";
    }

    @GetMapping("/addPost")
    public String moveAddPostPAge() {
        return "/html/addPost";
    }
}
