package com.example.manikantasales.dto;



public class CategoryResponse {


    private Long id;

    private String name;

    private String description;

    private String image;

    private boolean active;



    // ==========================
    // CONSTRUCTOR
    // ==========================

    public CategoryResponse(
            Long id,
            String name,
            String description,
            String image,
            boolean active
    ){

        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.active = active;

    }




    // ==========================
    // GETTERS
    // ==========================


    public Long getId(){

        return id;

    }



    public String getName(){

        return name;

    }



    public String getDescription(){

        return description;

    }



    public String getImage(){

        return image;

    }



    public boolean isActive(){

        return active;

    }




    // ==========================
    // SETTERS
    // ==========================


    public void setId(Long id){

        this.id = id;

    }



    public void setName(String name){

        this.name = name;

    }



    public void setDescription(String description){

        this.description = description;

    }



    public void setImage(String image){

        this.image = image;

    }



    public void setActive(boolean active){

        this.active = active;

    }


}