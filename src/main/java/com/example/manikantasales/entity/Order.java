package com.example.manikantasales.entity;

import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentMethod;
import com.example.manikantasales.enums.PaymentStatus;
import com.example.manikantasales.enums.ReturnStatus;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
public class Order {


    // =========================
    // RETURN STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus = ReturnStatus.NONE;


    // =========================
    // RETURN REASON
    // =========================

    @Column(length = 500)
    private String returnReason;



    // =========================
    // ORDER ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // =========================
    // USER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    // =========================
    // DELIVERY ADDRESS
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;



    // =========================
    // ORDER ITEMS
    // =========================

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems =
            new ArrayList<>();



    // =========================
    // TOTAL AMOUNT
    // =========================

    private BigDecimal amount;



    // =========================
    // ORDER STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;



    // =========================
    // PAYMENT STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;



    // =========================
    // PAYMENT METHOD
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;



    // =========================
    // CREATED DATE
    // =========================

    private LocalDateTime createdDate;



    // =========================
    // DELIVERY DATE
    // =========================

    private LocalDateTime deliveryDate;




    // =========================
    // CONSTRUCTOR
    // =========================

    public Order(){

    }




    // =========================
    // BEFORE INSERT
    // =========================

    @PrePersist
    public void onCreate(){


        if(createdDate == null){

            createdDate =
                    LocalDateTime.now();
        }


        if(orderStatus == null){

            orderStatus =
                    OrderStatus.PENDING;
        }


        if(paymentStatus == null){

            paymentStatus =
                    PaymentStatus.PENDING;
        }


        if(returnStatus == null){

            returnStatus =
                    ReturnStatus.NONE;
        }

    }




    // =========================
    // BEFORE UPDATE
    // =========================

    @PreUpdate
    public void onUpdate(){

        if(orderStatus == OrderStatus.DELIVERED
                && deliveryDate == null){

            deliveryDate =
                    LocalDateTime.now();
        }

    }





    // =========================
    // GETTERS SETTERS
    // =========================


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }



    public Address getAddress() {
        return address;
    }


    public void setAddress(Address address) {
        this.address = address;
    }



    public List<OrderItem> getOrderItems() {
        return orderItems;
    }


    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }



    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }



    public OrderStatus getOrderStatus() {
        return orderStatus;
    }


    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }



    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }


    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }



    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }



    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }



    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }


    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }



    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }


    public void setReturnStatus(ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }



    public String getReturnReason() {
        return returnReason;
    }


    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

}