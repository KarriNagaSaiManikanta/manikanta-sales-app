package com.example.manikantasales.service;

public interface OtpService {

    String generateOtp();

    void saveOtp(String email, String otp);

    void sendRegistrationOtp(String email, String otp);

    void sendForgotPasswordOtp(String email, String otp);

    String verifyOtp(String email, String otp);
}