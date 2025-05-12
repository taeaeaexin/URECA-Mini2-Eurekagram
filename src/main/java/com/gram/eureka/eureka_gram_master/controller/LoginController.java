package com.gram.eureka.eureka_gram_master.controller;



import com.gram.eureka.eureka_gram_master.dto.BaseResponseDto;
import com.gram.eureka.eureka_gram_master.dto.LoginRequestDto;
import com.gram.eureka.eureka_gram_master.dto.LoginResponseDto;
import com.gram.eureka.eureka_gram_master.entity.enums.Role;
import com.gram.eureka.eureka_gram_master.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @ResponseBody
    public BaseResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("Login request: {}", loginRequestDto);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        BaseResponseDto<LoginResponseDto> baseResponseDto = BaseResponseDto.<LoginResponseDto>builder()
                .data(null)
                .statusCode(999) // 임시 코드 값 설정 (차후 변경 필요)
                .message("로그인 실패")
                .build();

        try {
            // 이메일/비밀번호로 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            // 인증 성공 시 JWT 토큰 생성
            String token = jwtUtil.generateToken(authentication.getName(), Role.ROLE_ADMIN.getLabel());
            loginResponseDto.setToken(token);

            // 성공 응답 구성
            baseResponseDto.setStatusCode(200);
            baseResponseDto.setMessage("로그인 성공");
            baseResponseDto.setData(loginResponseDto);

        } catch (BadCredentialsException e) {
            log.warn("잘못된 자격 증명: {}", loginRequestDto.getEmail());
            baseResponseDto.setMessage("비밀번호가 일치하지 않습니다.");
        } catch (UsernameNotFoundException e) {
            log.warn("존재하지 않는 계정 : {}", loginRequestDto.getEmail());
            baseResponseDto.setMessage("존재하지 않는 이메일입니다.");
        } catch (AuthenticationException e) {
            log.error("기타 인증 실패: {}", e.getMessage());
            baseResponseDto.setMessage("로그인 중 오류가 발생했습니다.");
        }
        return baseResponseDto;
    }
}
