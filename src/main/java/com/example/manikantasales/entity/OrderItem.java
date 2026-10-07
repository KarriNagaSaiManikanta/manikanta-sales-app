package com.example.manikantasales.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name="order_items")
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // =========================
    // ORDER MAPPING
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;




    // =========================
    // PRODUCT MAPPING
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;




    // =========================
    // PRODUCT SNAPSHOT
    // =========================

    private String productName;


    private String productImage;


    private BigDecimal price;


    private Integer quantity;


    private BigDecimal subTotal;





    public OrderItem(){

    }





    public Long getId(){

        return id;

    }


    public void setId(Long id){

        this.id=id;

    }





    public Order getOrder(){

        return order;

    }


    public void setOrder(Order order){

        this.order=order;

    }





    public Product getProduct(){

        return product;

    }


    public void setProduct(Product product){

        this.product=product;

    }





    public String getProductName(){

        return productName;

    }


    public void setProductName(String productName){

        this.productName=productName;

    }





    public String getProductImage(){

        return productImage;

    }


    public void setProductImage(String productImage){

        this.productImage=productImage;

    }





    public BigDecimal getPrice(){

        return price;

    }


    public void setPrice(BigDecimal price){

        this.price=price;

    }





    public Integer getQuantity(){

        return quantity;

    }


    public void setQuantity(Integer quantity){

        this.quantity=quantity;

    }





    public BigDecimal getSubTotal(){

        return subTotal;

    }


    public void setSubTotal(BigDecimal subTotal){

        this.subTotal=subTotal;

    }

}