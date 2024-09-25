package com.heima.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@EnableFeignClients(basePackages = "com.heima.api.client")
@MapperScan("com.heima.cart.mapper")
@SpringBootApplication
public class CartallApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartallApplication.class, args);
    }
}