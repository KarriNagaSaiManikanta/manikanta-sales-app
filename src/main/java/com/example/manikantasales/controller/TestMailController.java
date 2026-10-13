package com.example.manikantasales.controller;

import com.example.manikantasales.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMailController {

    private final EmailService emailService;

    public TestMailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail-test")
    public String sendMail() {

        emailService.sendEmail(
                "YOUR_EMAIL@gmail.com",
                "Spring Boot Test",
                "Hello from Manikanta Sales!"
        );

        return "Mail Sent";
    }
}