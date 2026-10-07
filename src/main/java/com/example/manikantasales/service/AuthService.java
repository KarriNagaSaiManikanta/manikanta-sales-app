package com.example.manikantasales.service;


import com.example.manikantasales.dto.ForgotPasswordRequest;
import com.example.manikantasales.dto.LoginRequest;
import com.example.manikantasales.dto.LoginResponse;
import com.example.manikantasales.dto.RegisterRequest;
import com.example.manikantasales.dto.ResetPasswordRequest;
import com.example.manikantasales.dto.VerifyOtpRequest;


public interface AuthService {


    // ===========================
    // USER REGISTER
    // ===========================
    String register(RegisterRequest request);



    // ===========================
    // ADMIN REGISTER
    // ===========================
    String adminRegister(RegisterRequest request);



    // ===========================
    // VERIFY REGISTRATION OTP
    // ===========================
    String verifyOtp(VerifyOtpRequest request);



    // ===========================
    // RESEND REGISTRATION OTP
    // ===========================
    String resendOtp(ForgotPasswordRequest request);



    // ===========================
    // LOGIN USER + ADMIN
    // ===========================
    LoginResponse login(LoginRequest request);



    // ===========================
    // FORGOT PASSWORD
    // ===========================
    String forgotPassword(ForgotPasswordRequest request);



    // ===========================
    // RESET PASSWORD
    // ===========================
    String resetPassword(ResetPasswordRequest request);


}