package com.example.manikantasales.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(length = 1000)
    private String description;


    @Column(nullable = false)
    private BigDecimal price;


    private Integer quantity;


    private String image;


    private String brand;


    private boolean active = true;


    private boolean deleted = false;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;



    // =====================================
    // CATEGORY RELATION
    // =====================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;



    // =====================================
    // LIFE CYCLE
    // =====================================

    @PrePersist
    public void prePersist(){

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();

    }



    @PreUpdate
    public void preUpdate(){

        updatedAt = LocalDateTime.now();

    }



    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Product(){}



    // =====================================
    // GETTERS SETTERS
    // =====================================


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }



    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }



    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }



    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }



    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }



    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

}