package com.example.manikantasales.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${MAIL_USERNAME:NOT_FOUND}")
    private String username;

    @Value("${MAIL_PASSWORD:NOT_FOUND}")
    private String password;

    @GetMapping("/test-mail")
    public String testMail() {
        return "Username = " + username + "<br>Password = " + password;
    }
}