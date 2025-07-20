package com.gram.eureka.eureka_gram_master.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
