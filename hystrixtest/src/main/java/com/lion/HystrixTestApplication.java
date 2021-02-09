package com.lion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@SpringBootApplication
public class HystrixTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixTestApplication.class, args);
    }
}
