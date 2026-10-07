package com.example.manikantasales.dto;


import java.math.BigDecimal;
import com.example.manikantasales.enums.ReturnStatus;


public class OrderItemResponse {



    private Long productId;


    private String productName;


    private String productImage;


    private BigDecimal price;


    private Integer quantity;


    private BigDecimal subTotal;








    public OrderItemResponse(){


    }









    public Long getProductId(){

        return productId;

    }


    public void setProductId(Long productId){

        this.productId = productId;

    }








    public String getProductName(){

        return productName;

    }


    public void setProductName(String productName){

        this.productName = productName;

    }








    public String getProductImage(){

        return productImage;

    }


    public void setProductImage(String productImage){

        this.productImage = productImage;

    }








    public BigDecimal getPrice(){

        return price;

    }


    public void setPrice(BigDecimal price){

        this.price = price;

    }








    public Integer getQuantity(){

        return quantity;

    }


    public void setQuantity(Integer quantity){

        this.quantity = quantity;

    }








    public BigDecimal getSubTotal(){

        return subTotal;

    }


    public void setSubTotal(BigDecimal subTotal){

        this.subTotal = subTotal;

    }


}