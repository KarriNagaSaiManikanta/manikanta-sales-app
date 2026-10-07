package com.example.manikantasales.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="banners")
public class Banner {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;


    private String description;


    private String image;


    private String category;


    private String offer;


    private boolean active=true;


    private LocalDateTime createdAt;



    @PrePersist
    public void created(){

        createdAt=LocalDateTime.now();

    }



    public Long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
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


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getOffer() {
        return offer;
    }


    public void setOffer(String offer) {
        this.offer = offer;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


}