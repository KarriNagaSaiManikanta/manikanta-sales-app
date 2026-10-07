package com.example.manikantasales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ManikantasalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManikantasalesApplication.class, args);
    }

}