package com.example.manikantasales.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CategoryRequest {


    @NotBlank(
            message = "Category name is required"
    )
    @Size(
            min = 3,
            max = 50,
            message = "Category name must be between 3 and 50 characters"
    )
    private String name;


    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    private String description;


    // database image path
    private String imagePath;


    private boolean active = true;



    public CategoryRequest(){}



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



    public String getImagePath() {
        return imagePath;
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }

}