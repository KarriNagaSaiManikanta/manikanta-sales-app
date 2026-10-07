package com.example.manikantasales.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "cart",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "user_id",
                                "product_id"
                        }
                )
        }
)
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // =====================================
    // USER
    // =====================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;



    // =====================================
    // PRODUCT
    // =====================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;



    // =====================================
    // QUANTITY
    // =====================================

    @Column(nullable = false)
    private Integer quantity = 1;



    // =====================================
    // CREATED DATE
    // =====================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;



    // =====================================
    // UPDATED DATE
    // =====================================

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;




    // =====================================
    // AUTO DATE
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
    // CONSTRUCTORS
    // =====================================

    public Cart(){

    }



    public Cart(
            User user,
            Product product,
            Integer quantity
    ){

        this.user = user;

        this.product = product;

        this.quantity = quantity;

    }





    // =====================================
    // GETTERS & SETTERS
    // =====================================


    public Long getId(){

        return id;

    }


    public void setId(Long id){

        this.id = id;

    }




    public User getUser(){

        return user;

    }


    public void setUser(User user){

        this.user = user;

    }




    public Product getProduct(){

        return product;

    }


    public void setProduct(Product product){

        this.product = product;

    }




    public Integer getQuantity(){

        return quantity;

    }


    public void setQuantity(Integer quantity){

        this.quantity = quantity;

    }




    public LocalDateTime getCreatedAt(){

        return createdAt;

    }


    public void setCreatedAt(LocalDateTime createdAt){

        this.createdAt = createdAt;

    }




    public LocalDateTime getUpdatedAt(){

        return updatedAt;

    }


    public void setUpdatedAt(LocalDateTime updatedAt){

        this.updatedAt = updatedAt;

    }

}