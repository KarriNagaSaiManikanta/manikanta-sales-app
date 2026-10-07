package com.example.manikantasales.dto;

import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentMethod;
import com.example.manikantasales.enums.PaymentStatus;
import com.example.manikantasales.enums.ReturnStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    // ===================================
    // RETURN DETAILS
    // ===================================

    private ReturnStatus returnStatus;

    private String returnReason;


    // ===================================
    // CUSTOMER / USER DETAILS
    // ===================================

    private Long userId;

    private String customerName;

    private String customerEmail;


    // ===================================
    // ORDER DETAILS
    // ===================================

    private Long id;

    private BigDecimal amount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdDate;

    private LocalDateTime deliveryDate;

    private Integer totalItems;

    private AddressResponse address;

    private List<OrderItemResponse> items;


    // ===================================
    // CONSTRUCTOR
    // ===================================

    public OrderResponse() {

    }


    // ===================================
    // GET USER ID
    // ===================================

    public Long getUserId() {

        return userId;

    }


    // ===================================
    // SET USER ID
    // ===================================

    public void setUserId(Long userId) {

        this.userId = userId;

    }


    // ===================================
    // GET CUSTOMER NAME
    // ===================================

    public String getCustomerName() {

        return customerName;

    }


    // ===================================
    // SET CUSTOMER NAME
    // ===================================

    public void setCustomerName(String customerName) {

        this.customerName = customerName;

    }


    // ===================================
    // GET CUSTOMER EMAIL
    // ===================================

    public String getCustomerEmail() {

        return customerEmail;

    }


    // ===================================
    // SET CUSTOMER EMAIL
    // ===================================

    public void setCustomerEmail(String customerEmail) {

        this.customerEmail = customerEmail;

    }


    // ===================================
    // GET ID
    // ===================================

    public Long getId() {

        return id;

    }


    // ===================================
    // SET ID
    // ===================================

    public void setId(Long id) {

        this.id = id;

    }


    // ===================================
    // GET AMOUNT
    // ===================================

    public BigDecimal getAmount() {

        return amount;

    }


    // ===================================
    // SET AMOUNT
    // ===================================

    public void setAmount(BigDecimal amount) {

        this.amount = amount;

    }


    // ===================================
    // GET ORDER STATUS
    // ===================================

    public OrderStatus getOrderStatus() {

        return orderStatus;

    }


    // ===================================
    // SET ORDER STATUS
    // ===================================

    public void setOrderStatus(OrderStatus orderStatus) {

        this.orderStatus = orderStatus;

    }


    // ===================================
    // GET PAYMENT STATUS
    // ===================================

    public PaymentStatus getPaymentStatus() {

        return paymentStatus;

    }


    // ===================================
    // SET PAYMENT STATUS
    // ===================================

    public void setPaymentStatus(PaymentStatus paymentStatus) {

        this.paymentStatus = paymentStatus;

    }


    // ===================================
    // GET PAYMENT METHOD
    // ===================================

    public PaymentMethod getPaymentMethod() {

        return paymentMethod;

    }


    // ===================================
    // SET PAYMENT METHOD
    // ===================================

    public void setPaymentMethod(PaymentMethod paymentMethod) {

        this.paymentMethod = paymentMethod;

    }


    // ===================================
    // GET CREATED DATE
    // ===================================

    public LocalDateTime getCreatedDate() {

        return createdDate;

    }


    // ===================================
    // SET CREATED DATE
    // ===================================

    public void setCreatedDate(LocalDateTime createdDate) {

        this.createdDate = createdDate;

    }


    // ===================================
    // GET DELIVERY DATE
    // ===================================

    public LocalDateTime getDeliveryDate() {

        return deliveryDate;

    }


    // ===================================
    // SET DELIVERY DATE
    // ===================================

    public void setDeliveryDate(LocalDateTime deliveryDate) {

        this.deliveryDate = deliveryDate;

    }


    // ===================================
    // GET TOTAL ITEMS
    // ===================================

    public Integer getTotalItems() {

        return totalItems;

    }


    // ===================================
    // SET TOTAL ITEMS
    // ===================================

    public void setTotalItems(Integer totalItems) {

        this.totalItems = totalItems;

    }


    // ===================================
    // GET ADDRESS
    // ===================================

    public AddressResponse getAddress() {

        return address;

    }


    // ===================================
    // SET ADDRESS
    // ===================================

    public void setAddress(AddressResponse address) {

        this.address = address;

    }


    // ===================================
    // GET ITEMS
    // ===================================

    public List<OrderItemResponse> getItems() {

        return items;

    }


    // ===================================
    // SET ITEMS
    // ===================================

    public void setItems(List<OrderItemResponse> items) {

        this.items = items;

    }


    // ===================================
    // GET RETURN STATUS
    // ===================================

    public ReturnStatus getReturnStatus() {

        return returnStatus;

    }


    // ===================================
    // SET RETURN STATUS
    // ===================================

    public void setReturnStatus(ReturnStatus returnStatus) {

        this.returnStatus = returnStatus;

    }


    // ===================================
    // GET RETURN REASON
    // ===================================

    public String getReturnReason() {

        return returnReason;

    }


    // ===================================
    // SET RETURN REASON
    // ===================================

    public void setReturnReason(String returnReason) {

        this.returnReason = returnReason;

    }

}