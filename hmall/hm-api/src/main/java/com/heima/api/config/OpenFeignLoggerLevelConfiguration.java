package com.heima.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class OpenFeignLoggerLevelConfiguration {
    @Bean
    public Logger.Level openFeginLoggerLevel(){
        return Logger.Level.FULL;
    }
}
