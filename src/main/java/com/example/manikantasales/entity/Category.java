package com.example.manikantasales.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "categories")
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(length = 1000)
    private String description;


    private String image;


    @Column(nullable = false)
    private boolean active = true;


    @Column(nullable = false)
    private boolean deleted = false;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    @PrePersist
    public void prePersist(){

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }



    @PreUpdate
    public void preUpdate(){

        updatedAt = LocalDateTime.now();

    }



    public Category(){

    }



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



    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
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

}