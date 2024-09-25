package com.heima.trade;

import com.heima.api.config.OpenFeignLoggerLevelConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.heima.api.client", defaultConfiguration = OpenFeignLoggerLevelConfiguration.class)
@MapperScan("com.heima.trade.mapper")
@SpringBootApplication
public class TradeallApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeallApplication.class, args);
    }
}