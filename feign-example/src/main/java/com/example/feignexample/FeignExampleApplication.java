package com.example.feignexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignExampleApplication.class, args);
    }

}
