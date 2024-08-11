package com.heima.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.heima.cart.mapper")
@SpringBootApplication
public class CartallApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartallApplication.class, args);
    }
}