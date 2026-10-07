package com.example.manikantasales.entity;


import com.example.manikantasales.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "pending_users")
public class PendingUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;


    private String lastName;


    @Column(unique = true)
    private String email;


    private String password;


    @Column(unique = true)
    private String mobile;


    private String otp;


    private LocalDateTime otpExpiry;



    // USER / ADMIN
    @Enumerated(EnumType.STRING)
    private Role role;



    public PendingUser() {

    }



    public Long getId() {
        return id;
    }



    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }



    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getOtp() {
        return otp;
    }


    public void setOtp(String otp) {
        this.otp = otp;
    }



    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }


    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }




    // ===========================
    // ROLE GETTER SETTER
    // ===========================

    public Role getRole() {

        return role;

    }


    public void setRole(Role role) {

        this.role = role;

    }


}