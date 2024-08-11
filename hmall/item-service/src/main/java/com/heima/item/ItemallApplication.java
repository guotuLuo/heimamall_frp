package com.heima.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.heima.item.mapper")
@SpringBootApplication
public class ItemallApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemallApplication.class, args);
    }
}