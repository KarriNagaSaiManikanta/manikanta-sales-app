package com.example.manikantasales.dto;


import java.math.BigDecimal;



public class WishlistResponse {


    private Long id;

    private Long productId;

    private String name;

    private String image;

    private BigDecimal price;



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id=id;
    }



    public Long getProductId() {
        return productId;
    }


    public void setProductId(Long productId) {
        this.productId=productId;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name=name;
    }



    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image=image;
    }



    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price=price;
    }

}