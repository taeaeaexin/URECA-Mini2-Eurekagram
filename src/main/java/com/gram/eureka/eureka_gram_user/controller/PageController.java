package com.gram.eureka.eureka_gram_user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping("/add-feed")
    public String moveAddFeedPage() {
        return "/html/add-feed";
    }

    @GetMapping("/detail-feed")
    public String moveDetailFeedPage(@RequestParam(required = false) Long id) {
        log.info("moveDetailFeedPage : {}", id);
        return "/html/detail-feed";
    }

    @GetMapping("/my-feed")
    public String moveMyFeedPage() {
        return "/html/my-feed";
    }

    @GetMapping("/join")
    public String moveJoinPage() {
        return "/html/join";
    }

    @GetMapping("/modify-feed")
    public String moveModifyFeedPage() {
        return "/html/modify-feed";
    }
}
