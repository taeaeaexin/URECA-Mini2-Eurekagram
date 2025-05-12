package com.gram.eureka.eureka_gram_master.service.impl;

import com.gram.eureka.eureka_gram_master.entity.User;
import com.gram.eureka.eureka_gram_master.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findActiveUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(user.getRole().getLabel())) // 이미 "ROLE_" 접두사가 포함된 경우
                .build();
    }
}
