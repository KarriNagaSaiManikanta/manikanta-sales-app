package com.example.manikantasales.repository;

import com.example.manikantasales.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findByEmail(String email);

    Optional<OtpToken> findByEmailAndOtp(String email, String otp);

}