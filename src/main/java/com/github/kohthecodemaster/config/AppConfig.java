package com.github.kohthecodemaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stdlib.utils.MyTimer;

@Configuration
public class AppConfig {

    @Bean
    public MyTimer myTimer() {
        return new MyTimer();
    }
}
