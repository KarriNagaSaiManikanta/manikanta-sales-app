package com.example.manikantasales.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "addresses")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // ==========================
    // USER RELATION
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;




    // ==========================
    // CUSTOMER NAME
    // ==========================

    @Column(nullable = false)
    private String name;



    @Column(nullable = false)
    private String mobile;



    @Column(nullable = false)
    private String houseNo;



    @Column(nullable = false)
    private String street;



    @Column(nullable = false)
    private String city;



    @Column(nullable = false)
    private String state;



    @Column(nullable = false)
    private String pincode;



    @Column(nullable = false)
    private boolean defaultAddress = false;



    // ==========================
    // DATE
    // ==========================

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private LocalDateTime updatedAt;




    // ==========================
    // LIFE CYCLE
    // ==========================

    @PrePersist
    public void prePersist(){

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }



    @PreUpdate
    public void preUpdate(){

        updatedAt = LocalDateTime.now();

    }




    // ==========================
    // CONSTRUCTOR
    // ==========================

    public Address(){

    }





    // ==========================
    // GETTERS SETTERS
    // ==========================


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    @JsonIgnore
    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getHouseNo() {
        return houseNo;
    }


    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }



    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }



    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }



    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }



    public String getPincode() {
        return pincode;
    }


    public void setPincode(String pincode) {
        this.pincode = pincode;
    }



    public boolean isDefaultAddress() {
        return defaultAddress;
    }


    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}