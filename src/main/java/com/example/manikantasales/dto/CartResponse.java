package com.example.manikantasales.dto;


import java.math.BigDecimal;


public class CartResponse {


    private Long cartId;

    private Long productId;

    private String productName;

    private String productImage;

    private String brand;

    private String category;

    private BigDecimal price;

    private Integer quantity;

    private Integer stock;

    private BigDecimal totalPrice;



    // ============================
    // DEFAULT CONSTRUCTOR
    // ============================

    public CartResponse(){

    }



    // ============================
    // GETTERS SETTERS
    // ============================


    public Long getCartId() {
        return cartId;
    }


    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }



    public Long getProductId() {
        return productId;
    }


    public void setProductId(Long productId) {
        this.productId = productId;
    }



    public String getProductName() {
        return productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }



    public String getProductImage() {
        return productImage;
    }


    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }



    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }



    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
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



    public Integer getStock() {
        return stock;
    }


    public void setStock(Integer stock) {
        this.stock = stock;
    }



    public BigDecimal getTotalPrice() {
        return totalPrice;
    }


    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}