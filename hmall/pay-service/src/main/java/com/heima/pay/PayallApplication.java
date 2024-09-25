package com.heima.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.heima.api.client")
@MapperScan("com.heima.pay.mapper")
@SpringBootApplication
public class PayallApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayallApplication.class, args);
    }

}