package com.example.manikantasales.entity;

import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentMethod;
import com.example.manikantasales.enums.PaymentStatus;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.manikantasales.enums.ReturnStatus;


@Entity
@Table(name = "orders")
public class Order {

	
	@Enumerated(EnumType.STRING)
	private ReturnStatus returnStatus = ReturnStatus.NONE;
	
	public ReturnStatus getReturnStatus() {
	    return returnStatus;
	}
	
	public void setReturnStatus(ReturnStatus returnStatus) {
	    this.returnStatus = returnStatus;
	}
	
	
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
    private List<OrderItem> orderItems = new ArrayList<>();


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
    // CONSTRUCTOR
    // =========================

    public Order() {
    }


    // =========================
    // BEFORE SAVE
    // =========================

    
    @PrePersist
    public void onCreate() {

        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }

        if (orderStatus == null) {
            orderStatus = OrderStatus.PENDING;
        }

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }

        if (returnStatus == null) {
            returnStatus = ReturnStatus.NONE;
        }
    }

    // =========================
    // GET ID
    // =========================

    public Long getId() {
        return id;
    }


    // =========================
    // SET ID
    // =========================

    public void setId(Long id) {
        this.id = id;
    }


    // =========================
    // GET USER
    // =========================

    public User getUser() {
        return user;
    }


    // =========================
    // SET USER
    // =========================

    public void setUser(User user) {
        this.user = user;
    }


    // =========================
    // GET ADDRESS
    // =========================

    public Address getAddress() {
        return address;
    }


    // =========================
    // SET ADDRESS
    // =========================

    public void setAddress(Address address) {
        this.address = address;
    }


    // =========================
    // GET ORDER ITEMS
    // =========================

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }


    // =========================
    // SET ORDER ITEMS
    // =========================

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    // =========================
    // GET AMOUNT
    // =========================

    public BigDecimal getAmount() {
        return amount;
    }


    // =========================
    // SET AMOUNT
    // =========================

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    // =========================
    // GET ORDER STATUS
    // =========================

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }


    // =========================
    // SET ORDER STATUS
    // =========================

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    // =========================
    // GET PAYMENT STATUS
    // =========================

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }


    // =========================
    // SET PAYMENT STATUS
    // =========================

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    // =========================
    // GET PAYMENT METHOD
    // =========================

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    // =========================
    // SET PAYMENT METHOD
    // =========================

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    // =========================
    // GET CREATED DATE
    // =========================

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


    // =========================
    // SET CREATED DATE
    // =========================

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    @Column(length = 500)
    private String returnReason;
    
    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}