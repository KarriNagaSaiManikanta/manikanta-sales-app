package com.example.manikantasales.entity;


import jakarta.persistence.*;

@Entity
@Table(name="wishlist")
public class Wishlist {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;



    // GETTERS

    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public Product getProduct() {
        return product;
    }



    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public void setProduct(Product product) {
        this.product = product;
    }

}