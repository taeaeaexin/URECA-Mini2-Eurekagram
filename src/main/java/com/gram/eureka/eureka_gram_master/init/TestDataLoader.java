//package com.gram.eureka.eureka_gram_master.init;//package com.gram.eureka.eureka_gram_user.init;
//
//
//import com.gram.eureka.eureka_gram_master.entity.Comment;
//import com.gram.eureka.eureka_gram_master.entity.Feed;
//import com.gram.eureka.eureka_gram_master.entity.Image;
//import com.gram.eureka.eureka_gram_master.entity.User;
//import com.gram.eureka.eureka_gram_master.entity.enums.*;
//import com.gram.eureka.eureka_gram_master.repository.CommentRepository;
//import com.gram.eureka.eureka_gram_master.repository.FeedRepository;
//import com.gram.eureka.eureka_gram_master.repository.ImageRepository;
//import com.gram.eureka.eureka_gram_master.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataLoader {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final CommentRepository commentRepository;
//    private final FeedRepository feedRepository;
//    private final ImageRepository imageRepository;
//
//    @PostConstruct
//    public void init() {
//        createUser();
//        // createFeed();
//    }
//
//    // 피드 데이터 생성
//    public void createFeed() {
//        Optional<User> user = userRepository.findById(1L);
//
//        Feed feed1 = Feed.builder()
//                .content("테스트1 컨텐트 입니다.")
//                .user(user.get())
//                .build();
//
//        Feed feed2 = Feed.builder()
//                .content("테스트2 컨텐트 입니다.")
//                .user(user.get())
//                .build();
//
//        Image image1 = Image.builder()
//                .feed(feed1)
//                .originalImageName("테스트1-피드1 이미지")
//                .storedImageName("랜덤 이미지 데이터1")
//                .imageExtension(".png")
//                .build();
//
//        Image image2 = Image.builder()
//                .feed(feed1)
//                .originalImageName("테스트2-피드2 이미지")
//                .storedImageName("랜덤 이미지 데이터2")
//                .imageExtension(".png")
//                .build();
//
//        Comment comment1 = Comment.builder()
//                .feed(feed1)
//                .user(user.get())
//                .content("테스트 댓글1")
//                .build();
//
//        Comment comment2 = Comment.builder()
//                .feed(feed1)
//                .user(user.get())
//                .content("테스트 댓글2")
//                .build();
//
//        Comment comment3 = Comment.builder()
//                .feed(feed1)
//                .user(user.get())
//                .content("테스트 댓글3")
//                .build();
//
//
//        feedRepository.save(feed1);
//        feedRepository.save(feed2);
//        imageRepository.save(image1);
//        imageRepository.save(image2);
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//        commentRepository.save(comment3);
//    }
//
//    // 사용자 정보 데이터 생성
//    public void createUser() {
//        // ACTIVE 사용자 3명
//        for (int i = 1; i <= 3; i++) {
//            User user = User.builder()
//                    .userName("활성사용자" + i)
//                    .email("active" + i + "@naver.com")
//                    .password(passwordEncoder.encode("1234"))
//                    .nickName("active_user" + i)
//                    .phoneNumber("0101000" + String.format("%04d", i))
//                    .batch(Batch.SECOND)
//                    .track(Track.BACKEND)
//                    .mode(Mode.OFFLINE)
//                    .status(Status.ACTIVE)
//                    .role(Role.ROLE_USER)
//                    .build();
//
//            userRepository.save(user);
//        }
//
////        // PENDING 사용자 2명
////        for (int i = 1; i <= 2; i++) {
////            User user = User.builder()
////                    .userName("대기사용자" + i)
////                    .email("pending" + i + "@naver.com")
////                    .password(passwordEncoder.encode("1234"))
////                    .nickName("pending_user" + i)
////                    .phoneNumber("0102000" + String.format("%04d", i))
////                    .batch(Batch.SECOND)
////                    .track(Track.BACKEND)
////                    .mode(Mode.OFFLINE)
////                    .status(Status.PENDING)
////                    .role(Role.ROLE_USER)
////                    .build();
////
////            userRepository.save(user);
////        }
////
////        // INACTIVE 사용자 2명
////        for (int i = 1; i <= 2; i++) {
////            User user = User.builder()
////                    .userName("비활성사용자" + i)
////                    .email("inactive" + i + "@naver.com")
////                    .password(passwordEncoder.encode("1234"))
////                    .nickName("inactive_user" + i)
////                    .phoneNumber("0103000" + String.format("%04d", i))
////                    .batch(Batch.SECOND)
////                    .track(Track.BACKEND)
////                    .mode(Mode.OFFLINE)
////                    .status(Status.INACTIVE)
////                    .role(Role.ROLE_USER)
////                    .build();
////
////            userRepository.save(user);
////        }
////
//        // 관리자 1명
//        User admin = User.builder()
//                .userName("관리자")
//                .email("admin@naver.com")
//                .password(passwordEncoder.encode("1234"))
//                .nickName("test_admin")
//                .phoneNumber("01099998888")
//                .status(Status.ACTIVE)
//                .role(Role.ROLE_ADMIN)
//                .build();
//
//        userRepository.save(admin);
//    }
//}
