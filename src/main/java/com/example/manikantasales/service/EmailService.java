package com.example.manikantasales.service;

import com.example.manikantasales.entity.Order;

public interface EmailService {

    // =====================================
    // COMMON EMAIL
    // =====================================

    void sendEmail(
            String to,
            String subject,
            String body
    );


    // =====================================
    // OTP EMAIL
    // =====================================

    void sendOtpEmail(
            String email,
            String otp
    );


    // =====================================
    // WELCOME EMAIL
    // =====================================

    void sendWelcomeEmail(
            String email,
            String firstName
    );


    // =====================================
    // FORGOT PASSWORD OTP
    // =====================================

    void sendForgotPasswordOtp(
            String email,
            String otp
    );


    // =====================================
    // ORDER CONFIRMATION EMAIL
    // =====================================

    void sendOrderConfirmationEmail(
            Order order
    );


    // =====================================
    // ORDER CANCELLATION EMAIL
    // =====================================

    void sendOrderCancellationEmail(
            Order order
    );


    // =====================================
    // RETURN REQUEST EMAIL
    // =====================================

    void sendReturnRequestEmail(
            Order order
    );
}