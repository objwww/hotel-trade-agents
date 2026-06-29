package com.hotel.supply;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hotel")
public class SupplyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupplyApplication.class,args);
    }
}
