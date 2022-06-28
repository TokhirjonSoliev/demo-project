package com.exadel.coolDesking.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JwtSecretKey {

    private final JwtConfig jwtConfig;

    @Bean
    public byte[] secretKey(){
        return jwtConfig.getSecretKey().getBytes();
    }
}
