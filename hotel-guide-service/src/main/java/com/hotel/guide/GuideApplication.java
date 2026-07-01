package com.hotel.guide;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hotel.guide.infrastructure.persistence.mapper")
@SpringBootApplication(scanBasePackages = "com.hotel")
public class GuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuideApplication.class, args);
    }
}